package jp.scid.bio.store;

import static jp.scid.bio.store.jooq.Tables.*;

import java.util.List;

import jp.scid.bio.store.collection.CollectionType;
import jp.scid.bio.store.collection.SequenceCollection;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.Condition;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.impl.Factory;

public class SequenceLibrary {
    private final Factory create;
    
    private final DefaultSequenceCollectionList foldersRoot;
    
    private final RecordMapper<FolderRecord, SequenceCollection> folderCollectionMapper =
            new RecordMapper<FolderRecord, SequenceCollection>() {
        
        @Override
        public SequenceCollection map(FolderRecord folder) {
            CollectionType type = CollectionType.fromRecordValue(folder.getType());
            SequenceCollection sequenceCollection = type.createSequenceCollection(create, folder.getId());
            return sequenceCollection;
        }
    };
    
    SequenceLibrary(Factory factory) {
        this.create = factory;
        
        foldersRoot = new DefaultSequenceCollectionList(this, null);
    }
    
    public SequenceCollection getFolderContent(long folderId) {
        FolderRecord folder = findFolder(folderId);
        CollectionType type = CollectionType.fromRecordValue(folder.getType());
        SequenceCollection sequenceCollection = type.createSequenceCollection(create, folderId);
        
        return sequenceCollection;
    }
    
    protected List<SequenceCollection> fetchCollections(Long parentId) {
        return retrieveFolders(parentId).map(folderCollectionMapper);
    }
    
    public SequenceCollectionList getRootCollectionList() {
        foldersRoot.fetch();
        return foldersRoot;
    }
    
    public GeneticSequenceRecord createRecord() {
        GeneticSequenceRecord newRecord = create.newRecord(Tables.GENETIC_SEQUENCE);
        newRecord.setId(null);
        newRecord.setName("Untitiled");
        
        return newRecord;
    }

    public boolean addNew(GeneticSequenceRecord record) {
        int result = create.executeInsert(record);
        return result > 0;
    }

    public boolean delete(GeneticSequenceRecord record) {
        return create.executeDelete(record) > 0;
    }

    public List<GeneticSequenceRecord> findAll() {
        return create.fetch(Tables.GENETIC_SEQUENCE);
    }
    
    public boolean store(GeneticSequenceRecord record) {
        return create.executeUpdate(record) > 0;
    }
    
    @Deprecated
    public List<FolderRecord> getFolders(Long parentId) {
        return retrieveFolders(parentId);
    }

    private Result<FolderRecord> retrieveFolders(Long parentId) {
        Condition parentCondition;
        if (parentId == null) {
            parentCondition = FOLDER.PARENT_ID.isNull();
        }
        else {
            parentCondition = FOLDER.PARENT_ID.eq(parentId);
        }
        
        Result<FolderRecord> result = create
                .selectFrom(Tables.FOLDER)
                .where(parentCondition)
                .fetch();
        return result;
    }
    
    public int delete(List<GeneticSequenceRecord> list) {
        int count = 0;
        for (GeneticSequenceRecord record: list) {
            count += record.delete();
        }
        return count;
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
}


