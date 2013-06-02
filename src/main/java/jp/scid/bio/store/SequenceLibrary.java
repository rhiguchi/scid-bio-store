package jp.scid.bio.store;

import static jp.scid.bio.store.jooq.Tables.*;

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

import jp.scid.bio.store.base.AbstractRecordListModel;
import jp.scid.bio.store.folder.CollectionType;
import jp.scid.bio.store.folder.Folder;
import jp.scid.bio.store.folder.FolderList;
import jp.scid.bio.store.folder.FoldersContainer;
import jp.scid.bio.store.folder.JooqFolderSource;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;
import jp.scid.bio.store.sequence.FileLibrary;
import jp.scid.bio.store.sequence.GeneticSequence;
import jp.scid.bio.store.sequence.JooqGeneticSequence;
import jp.scid.bio.store.sequence.LibrarySequenceCollection;
import jp.scid.bio.store.sequence.SequenceCollection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.jooq.Field;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;

public class SequenceLibrary {
    private final static Field<String> tableNameField = Factory.field("table_name", String.class);
    private final static Field<String> tableSchemaField = Factory.field("table_schema", String.class);
    
    private final Factory create;
    
    private final UserFoldersRoot usersFolderRoot;
    
    private final LibrarySequenceCollection allSequences;
    
    private final FileLibrary sequences;
    
    private final JooqFolderSource folderSource;
    
    SequenceLibrary(Factory factory) {
        this.create = factory;
        
        sequences = new FileLibrary();
        
        folderSource = new JooqFolderSource(sequences, factory);
        
        allSequences = new LibrarySequenceCollection(sequences, factory);
        
        usersFolderRoot = new UserFoldersRoot();
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
    public SequenceCollection<GeneticSequence> getAllSequences() {
        return allSequences;
    }
    
    public SequenceCollection<GeneticSequence> fetchSequences() {
        allSequences.fetch();
        return allSequences;
    }

    public GeneticSequence importSequence(File file) throws IOException, ParseException {
        GeneticSequenceRecord sequenceRecord = create.newRecord(Tables.GENETIC_SEQUENCE);
        sequenceRecord.setName("Untitiled");
        
        JooqGeneticSequence sequence = new JooqGeneticSequence(sequenceRecord, sequences);
        sequence.setFileUri(file);
        
        sequence.reload();
        sequence.save();
        
        allSequences.add(sequence);
        
        return sequence;
    }

    public void setFilesStoreRoot(File filesStoreDirectory) {
        sequences.setSequenceFilesRoot(filesStoreDirectory);
    }
    
    public GeneticSequence deleteSequenceAt(int index) {
        GeneticSequence sequence = allSequences.removeElementAt(index);
        // TODO delete file
        sequence.delete();
        
        return sequence;
    }

    // Folders
    public UserFoldersRoot getUsersFolderRoot() {
        return usersFolderRoot;
    }
    
    @Deprecated
    public FolderList getRootFolderList() {
        return usersFolderRoot.rootFolderList;
    }
    
    @Deprecated
    public Folder addFolder(CollectionType type) {
        Folder folder = folderSource.createFolder(type, null);
        folder.save();
        usersFolderRoot.rootFolderList.add(folder);
        
        return folder;
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

    private void setUpSchema() {
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
    
    class UserFoldersRoot implements FoldersContainer {
        private final RootFolderList rootFolderList;
        
        UserFoldersRoot() {
            rootFolderList = new RootFolderList();
        }
        
        @Override
        public FolderList getContentFolders() {
            return rootFolderList;
        }

        @Override
        public Folder createContentFolder(CollectionType type) {
            Folder folder = folderSource.createFolder(type, null);
            rootFolderList.add(folder);
            
            return folder;
        }

        @Override
        public Folder removeContentFolderAt(int index) {
            return rootFolderList.removeElementAt(index);
        }

        @Override
        public boolean removeContentFolder(Folder folder) {
            return rootFolderList.removeElement(folder);
        }

        @Override
        public void addContentFolder(Folder folder) {
            rootFolderList.add(folder);
        }
    }

    // Contents
    private class RootFolderList extends AbstractRecordListModel<Folder> implements FolderList {
        @Override
        protected List<Folder> retrieve() {
            return folderSource.retrieveRootFolders(usersFolderRoot);
        }
    }
}

