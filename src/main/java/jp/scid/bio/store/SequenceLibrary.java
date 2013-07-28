package jp.scid.bio.store;

import static jp.scid.bio.store.jooq.Tables.*;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.sql.Connection;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.event.ChangeListener;

import jp.scid.bio.store.base.ChangeEventSupport;
import jp.scid.bio.store.folder.FoldersRoot;
import jp.scid.bio.store.folder.JooqFolderSource;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;
import jp.scid.bio.store.sequence.FileLibrary;
import jp.scid.bio.store.sequence.GeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequenceRecordMapper;
import jp.scid.bio.store.sequence.GeneticSequenceSource;
import jp.scid.bio.store.sequence.ImportableSequenceSource;
import jp.scid.bio.store.sequence.JooqGeneticSequence;
import jp.scid.bio.store.sequence.LibrarySequenceCollection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.jooq.Field;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;

public class SequenceLibrary implements GeneticSequenceSource, ImportableSequenceSource {
    private final static Field<String> tableNameField = Factory.field("table_name", String.class);
    private final static Field<String> tableSchemaField = Factory.field("table_schema", String.class);
    
    private final ChangeEventSupport sequenceChangeSupport;
    
    private final Factory create;
    
    private final FoldersRoot usersFolderRoot;
    
    private final LibrarySequenceCollection allSequences;
    
    private final FileLibrary sequences;
    
    private final JooqFolderSource folderSource;
    
    SequenceLibrary(Factory factory) {
        this.create = factory;
        
        sequences = new FileLibrary();
        
        folderSource = new JooqFolderSource(sequences, factory);
        
        allSequences = new LibrarySequenceCollection(sequences, factory);
        
        usersFolderRoot = new FoldersRoot(folderSource);
        
        sequenceChangeSupport = new ChangeEventSupport(this);
    }
    
    public static SequenceLibrary create(Connection connection) {
        Factory factory = new H2Factory(connection);
        SequenceLibrary library = new SequenceLibrary(factory);
        
        if (!library.isSchemaReady()) {
            library.setUpSchema();
        }
        
        return library;
    }
    
    // Sequences
    public List<GeneticSequence> getGeneticSequences() {
        return retrieve();
    }

    public void addSequencesChangeListener(ChangeListener listener) {
        sequenceChangeSupport.addChangeListener(listener);
    }
    
    public void removeSequencesChangeListener(ChangeListener listener) {
        sequenceChangeSupport.removeChangeListener(listener);
    }
    
    private List<GeneticSequence> retrieve() {
        Result<GeneticSequenceRecord> result = create.selectFrom(Tables.GENETIC_SEQUENCE)
                .orderBy(Tables.GENETIC_SEQUENCE.ID)
                .fetch();
        return result.map(new GeneticSequenceRecordMapper(sequences));
    }

    public GeneticSequence importSequence(File file) throws IOException, ParseException {
        JooqGeneticSequence sequence = createFromFile(file);
        sequence.reload();
        sequence.save();
        sequenceChangeSupport.fireStateChange();
        
        sequence.saveFileToLibrary();
        sequenceChangeSupport.fireStateChange();
        
        return sequence;
    }
    
//    @Override
    public JooqGeneticSequence createFromFile(File file) {
        JooqGeneticSequence sequence = createGeneticSequence();
        sequence.setFileUri(file);
        return sequence;
    }
    
//    @Override
    public void addGeneticSequence(GeneticSequence geneticSequence) {
        geneticSequence.save();
        sequenceChangeSupport.fireStateChange();
    }
    
    public boolean deleteSequence(GeneticSequence sequence, boolean withFile) {
        if (withFile && sequence.getFile() != null) {
            sequence.getFile().delete();
        }
        
        try {
            return sequence.delete();
        }
        finally {
            sequenceChangeSupport.fireStateChange();
        }
    }
    
    public Callable<GeneticSequence> createSequenceImportTask(File file) {
        final JooqGeneticSequence sequence = createGeneticSequence();
        sequence.setFileUri(file);
        
        final Runnable modelAppender = new Runnable() {
            public void run() {
                allSequences.add(sequence);
            }
        };
        
        Callable<GeneticSequence> loader = new Callable<GeneticSequence>() {
            @Override
            public GeneticSequence call() throws Exception {
                sequence.reload();
                sequence.save();
                if (EventQueue.isDispatchThread()) {
                    modelAppender.run();
                }
                else {
                    EventQueue.invokeAndWait(modelAppender);
                }
                return sequence;
            }
        };
        return loader;
    }

    private JooqGeneticSequence createGeneticSequence() {
        GeneticSequenceRecord sequenceRecord = create.newRecord(Tables.GENETIC_SEQUENCE);
        sequenceRecord.setName("Untitiled");
        
        return new JooqGeneticSequence(sequenceRecord, sequences);
    }

    public void setFilesStoreRoot(File filesStoreDirectory) {
        sequences.setSequenceFilesRoot(filesStoreDirectory);
    }
    
    // Folders
    public FoldersRoot getUsersFolderRoot() {
        return usersFolderRoot;
    }
    
    FolderRecord findFolder(long id) {
        return create.selectFrom(FOLDER)
                .where(FOLDER.ID.eq(id))
                .fetchOne();
    }
    
    Long getParentId(long boxId) {
        return create.select(FOLDER.PARENT_ID).from(FOLDER)
                .where(FOLDER.ID.equal(boxId))
                .fetchOne(FOLDER.PARENT_ID);
    }

    private boolean isSchemaReady() {
        List<String> names = getTableNames();
        return !names.isEmpty();
    }

    private List<String> getTableNames() {
        List<String> tableNames = create.select(tableNameField)
                .from("information_schema.tables")
                .where(tableSchemaField.eq("PUBLIC"))
                .fetch(tableNameField);
        return tableNames;
    }

    void setUpSchema() {
        // build schema tables
        String sql;
        try {
            sql = getSchemaSql();
        }
        catch (IOException e) {
            throw new DataAccessException("cannot retriece schema sql resource", e);
        }

        create.execute(sql);
    }
    
    @Override
    public String toString() {
        return "Local Files";
    }

    @SuppressWarnings("unused")
    private static Collection<File> listFiles(File file) {
        Collection<File> files;
        
        if (file.isDirectory()) {
            files = FileUtils.listFiles(file, HiddenFileFilter.VISIBLE, HiddenFileFilter.VISIBLE);
        }
        else {
            files = Collections.singleton(file);
        }
        
        return files;
    }
    
    private static String getSchemaSql() throws IOException {
        InputStream resource = SequenceLibrary.class.getResourceAsStream("sql/schema.sql");
        Reader reader = new InputStreamReader(resource);
        
        StringBuilder sql = new StringBuilder();
        CharBuffer buf = CharBuffer.allocate(8196);
        try {
            while (reader.read(buf) >= 0) {
                sql.append(buf.flip());
                buf.clear();
            }
        }
        finally {
            reader.close();
        }
        
        return sql.toString();
    }
}

