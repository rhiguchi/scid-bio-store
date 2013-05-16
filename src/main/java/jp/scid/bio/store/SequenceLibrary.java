package jp.scid.bio.store;

import static jp.scid.bio.store.jooq.Tables.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jp.scid.bio.store.base.AbstractRecordListModel;
import jp.scid.bio.store.folder.CollectionType;
import jp.scid.bio.store.folder.Folder;
import jp.scid.bio.store.folder.FolderList;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;
import jp.scid.bio.store.sequence.DefaultGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;
import jp.scid.bio.store.sequence.LibrarySequenceCollection;
import jp.scid.bio.store.sequence.SequenceCollection;

import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.impl.Factory;

public class SequenceLibrary {
    private final Factory create;
    
    private final RootFolderList rootFolderList;
    
    private final LibrarySequenceCollection allSequences;
    
    private final GeneticSequenceParser parser;
    
    SequenceLibrary(Factory factory) {
        this.create = factory;
        
        parser = new GeneticSequenceParser();
        
        allSequences = new LibrarySequenceCollection(factory);
        
        rootFolderList = new RootFolderList();
    }
    
    // Sequences
    public SequenceCollection<GeneticSequence> getAllSequences() {
        return allSequences;
    }

    public GeneticSequence importSequence(File file) throws IOException {
        GeneticSequenceRecord sequenceRecord = create.newRecord(Tables.GENETIC_SEQUENCE);
        sequenceRecord.setName("Untitiled");
        DefaultGeneticSequence sequence = new DefaultGeneticSequence(sequenceRecord);

        sequence.loadFrom(file, parser);
        sequence.save();
        allSequences.add(sequence);
        
        return sequence;
    }

    public GeneticSequence deleteSequenceAt(int index) {
        GeneticSequence sequence = allSequences.remove(index);
        sequence.delete();
        return sequence;
    }

    // Folders
    public FolderList getRootFolderList() {
        return rootFolderList;
    }
    
    public Folder addFolder(CollectionType type) {
        FolderRecord folderRecord = create.newRecord(Tables.FOLDER);
        folderRecord.setName(getNewFolderName(type));
        
        Folder folder = type.createFolder(folderRecord);
        folder.save();
        rootFolderList.add(folder);
        
        return folder;
    }
    
    public Folder deleteFolderAt(int index) {
        Folder folder = rootFolderList.remove(index);
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
    
    public boolean deleteFolder(long folderId) {
        return create.delete(FOLDER)
                .where(FOLDER.ID.eq(folderId))
                .execute() > 0;
    }
    
    public FolderRecord createFolder(CollectionType type) {
        FolderRecord record = create.newRecord(Tables.FOLDER);
        record.setId(null);
        record.setValue(FOLDER.TYPE, type, CollectionType.getConverter());
        record.setName(getNewFolderName(type));
        return record;
    }

    public void insertFolder(FolderRecord folder) {
        folder.store();
    }
    
    protected String getNewFolderName(CollectionType type) {
        return "New " + type.name(); // TODO
    }
    
    // Contents
    public List<GeneticSequenceRecord> fetchContent(long folderId) {
        List<GeneticSequenceRecord> elements = create.select()
                .from(Tables.COLLECTION_ITEM)
                .join(Tables.GENETIC_SEQUENCE)
                .on(COLLECTION_ITEM.GENETIC_SEQUENCE_ID.equal(GENETIC_SEQUENCE.ID))
                .where(COLLECTION_ITEM.FOLDER_ID.equal(folderId))
                .orderBy(COLLECTION_ITEM.ID)
                .fetchInto(Tables.GENETIC_SEQUENCE);
        return elements;
    }
    
    public GeneticSequenceRecord addContentTo(long boxId, long exhibitId) {
        CollectionItemRecord record = create.insertInto(COLLECTION_ITEM)
                .set(COLLECTION_ITEM.FOLDER_ID, boxId)
                .set(COLLECTION_ITEM.GENETIC_SEQUENCE_ID, exhibitId)
                .returning().fetchOne();
        
        long recordId = record.getId();
        
        GeneticSequenceRecord exhibit = create.select()
                .from(Tables.COLLECTION_ITEM)
                .join(Tables.GENETIC_SEQUENCE)
                .on(COLLECTION_ITEM.GENETIC_SEQUENCE_ID.equal(GENETIC_SEQUENCE.ID))
                .where(COLLECTION_ITEM.ID.equal(recordId))
                .fetchInto(Tables.GENETIC_SEQUENCE)
                .get(0);
        
        return exhibit;
    }
    
    public boolean removeContent(long contentId) {
        int result = create.delete(COLLECTION_ITEM)
                .where(COLLECTION_ITEM.ID.equal(contentId))
                .execute();
        return result > 0;
    }
    
    private class RootFolderList extends AbstractRecordListModel<Folder>
            implements FolderList, RecordMapper<FolderRecord, Folder> {
        @Override
        protected List<Folder> retrieve() {
            Result<FolderRecord> result = create.selectFrom(Tables.FOLDER)
                    .where(FOLDER.PARENT_ID.isNull())
                    .fetch();

            return result.map(this);
        }

        @Override
        public final Folder map(FolderRecord record) {
            CollectionType type = CollectionType.fromRecordValue(record.getType());
            return type.createSequenceCollection(record);
        }
    }
}


