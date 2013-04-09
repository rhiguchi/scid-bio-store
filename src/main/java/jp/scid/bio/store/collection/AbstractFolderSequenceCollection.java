package jp.scid.bio.store.collection;

import static org.jooq.impl.Factory.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.Factory;

abstract class AbstractFolderSequenceCollection extends AbstractSequenceCollection {
    final long folderId;
    
    public AbstractFolderSequenceCollection(Factory factory, long folderId) {
        super(factory);
        
        this.folderId = folderId;
    }
}

class BasicSequenceCollection extends AbstractFolderSequenceCollection {
    final static Field<Long> LAST_MODIFICATION = fieldByName(Long.class, "last_modification");
    final static Field<String> TABLE_NAME = fieldByName(String.class, "table_name");
    
    public BasicSequenceCollection(Factory factory, long folderId) {
        super(factory, folderId);
    }
    
    @Override
    protected List<IdentifiableRecord> retrieve() {
        Field<Long> collectionItemIdField = Tables.COLLECTION_ITEM.ID.as("collection_item_id");
        LinkedList<Field<?>> fields = new LinkedList<Field<?>>(Tables.GENETIC_SEQUENCE.getFields());
        fields.addFirst(collectionItemIdField);
        
        Result<Record> result = create.select(fields)
                .from(Tables.COLLECTION_ITEM)
                .join(Tables.GENETIC_SEQUENCE)
                .on(Tables.COLLECTION_ITEM.GENETIC_SEQUENCE_ID.eq(Tables.GENETIC_SEQUENCE.ID))
                .orderBy(Tables.COLLECTION_ITEM.ID)
                .fetch();
        
        List<IdentifiableRecord> list = result.map(new IdentifiableRecordMapper(collectionItemIdField));
        return list;
    }
    
    public GeneticSequenceRecord addFile(File file) throws IOException {
        GeneticSequenceRecord newRecord = create.newRecord(Tables.GENETIC_SEQUENCE);
        newRecord.setId(null);
        newRecord.setName("Untitiled");
        
        addRecord(newRecord);
        
        return newRecord;
    }
    
    public void addRecord(GeneticSequenceRecord record) {
        if (record == null) throw new IllegalArgumentException("record must not be null");
        
        record.store();
        
        CollectionItemRecord item = create.newRecord(Tables.COLLECTION_ITEM);
        item.setFolderId(folderId);
        item.setGeneticSequenceId(record);
        item.store();

        Long lookupId = item.getId();
        
        addOrUpdate(lookupId, record);
    }
    
    private long retrieveTableLastModification() {
        long value = create.select(LAST_MODIFICATION)
                .from(tableByName("INFORMATION_SCHEMA", "TABLES"))
                .where(TABLE_NAME.eq("GENETIC_SEQUENCE"))
                .fetchOne(LAST_MODIFICATION);
        return value;
    }
}

class FilterSequenceCollection extends AbstractFolderSequenceCollection {
    
    public FilterSequenceCollection(Factory factory, long folderId) {
        super(factory, folderId);
    }
    
    @Override
    protected List<IdentifiableRecord> retrieve() {
        // TODO Auto-generated method stub
        return Collections.emptyList();
    }
    
}
