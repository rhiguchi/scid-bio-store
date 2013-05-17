package jp.scid.bio.store.folder;

import static jp.scid.bio.store.jooq.Tables.*;

import java.util.LinkedList;
import java.util.List;

import jp.scid.bio.store.folder.FolderRecordGroupFolder.Source;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.impl.Factory;

public class JooqFolderSource implements Source {
    private final Factory create;
    
    private final RecordMapper<FolderRecord, Folder> folderRecordMapper = new RecordMapper<FolderRecord, Folder>() {
        @Override
        public final Folder map(FolderRecord record) {
            CollectionType type = CollectionType.fromRecordValue(record.getType());
            return type.createSequenceCollection(record, JooqFolderSource.this);
        }
    };
    
    public JooqFolderSource(Factory factory) {
        super();
        this.create = factory;
    }

    @Override
    public Folder createFolder(CollectionType type, Long parentFolderId) {
        if (type == null) throw new IllegalArgumentException("type must not be null");
        
        FolderRecord folderRecord = create.newRecord(Tables.FOLDER);
        folderRecord.setName(getNewFolderName(type));
        folderRecord.setType(type.getDbValue());
        
        Folder folder = type.createFolder(folderRecord, this);
        return folder;
    }
    
    protected String getNewFolderName(CollectionType type) {
        return "New " + type.name(); // TODO
    }

    boolean isDescend(Long folderId, Long folderId2) {
        // TODO Auto-generated method stub
        return false;
    }
    
    public List<Folder> retrieveFolderChildren(long parentFolderId) {
        Result<FolderRecord> result = create.selectFrom(Tables.FOLDER)
                .where(FOLDER.PARENT_ID.eq(parentFolderId))
                .fetch();
        return result.map(folderRecordMapper);
    }
    
    @Override
    public List<FolderContentGeneticSequence> retrieveFolderContents(long folderId) {
        LinkedList<Field<?>> fields = new LinkedList<Field<?>>(Tables.GENETIC_SEQUENCE.getFields());
        fields.addAll(0, Tables.COLLECTION_ITEM.getFields());
        
        Result<Record> result = create.select(fields)
                .from(Tables.COLLECTION_ITEM)
                .join(Tables.GENETIC_SEQUENCE)
                .on(Tables.COLLECTION_ITEM.GENETIC_SEQUENCE_ID.eq(Tables.GENETIC_SEQUENCE.ID))
                .where(Tables.COLLECTION_ITEM.FOLDER_ID.eq(folderId))
                .orderBy(Tables.COLLECTION_ITEM.ID)
                .fetch();
        return result.map(FolderContentGeneticSequenceMapper.basicMapper());
    }
}