package jp.scid.bio.store;

import static jp.scid.bio.store.jooq.Tables.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jp.scid.bio.store.base.AbstractRecordListModel;
import jp.scid.bio.store.folder.CollectionType;
import jp.scid.bio.store.folder.Folder;
import jp.scid.bio.store.folder.FolderList;
import jp.scid.bio.store.folder.JooqFolderSource;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;
import jp.scid.bio.store.sequence.GeneticSequence;
import jp.scid.bio.store.sequence.JooqGeneticSequence;
import jp.scid.bio.store.sequence.LibrarySequenceCollection;
import jp.scid.bio.store.sequence.SequenceCollection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.jooq.impl.Factory;

public class SequenceLibrary {
    private final Factory create;
    
    private final RootFolderList rootFolderList;
    
    private final LibrarySequenceCollection allSequences;
    
    private final FileLibrary sequences;
    
    private final JooqFolderSource folderSource;
    
    SequenceLibrary(Factory factory) {
        this.create = factory;
        
        sequences = new FileLibrary();
        
        folderSource = new JooqFolderSource(sequences, factory);
        
        allSequences = new LibrarySequenceCollection(sequences, factory);
        
        rootFolderList = new RootFolderList();
    }
    
    // Sequences
    public SequenceCollection<GeneticSequence> getAllSequences() {
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

    public GeneticSequence deleteSequenceAt(int index) {
        GeneticSequence sequence = allSequences.removeElementAt(index);
        // TODO delete file
        sequence.delete();
        
        return sequence;
    }

    // Folders
    public FolderList getRootFolderList() {
        return rootFolderList;
    }
    
    public Folder addFolder(CollectionType type) {
        Folder folder = folderSource.createFolder(type, null);
        folder.save();
        rootFolderList.add(folder);
        
        return folder;
    }
    
    public Folder deleteFolderAt(int index) {
        Folder folder = rootFolderList.removeElementAt(index);
        folder.delete();
        return folder;
    }
    
    public FolderRecord findFolder(long id) {
        return create.selectFrom(FOLDER)
                .where(FOLDER.ID.eq(id))
                .fetchOne();
    }
    
    private boolean isNodeFolder(long folderId) {
        Short nodeValue = CollectionType.NODE.getDbValue();
        
        return create.selectCount().from(FOLDER)
                .where(FOLDER.ID.eq(folderId))
                .and(FOLDER.TYPE.eq(nodeValue))
                .fetchOne(0, Integer.class)
                .intValue() > 0;
    }

    public Long getParentId(long boxId) {
        return create.select(FOLDER.PARENT_ID).from(FOLDER)
                .where(FOLDER.ID.equal(boxId))
                .fetchOne(FOLDER.PARENT_ID);
    }
    
    // Contents
    private class RootFolderList extends AbstractRecordListModel<Folder> implements FolderList {
        @Override
        protected List<Folder> retrieve() {
            return folderSource.retrieveRootFolders();
        }
    }

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
}

