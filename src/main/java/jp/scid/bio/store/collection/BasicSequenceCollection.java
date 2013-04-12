package jp.scid.bio.store.collection;

import static org.jooq.impl.Factory.*;

import java.util.LinkedList;
import java.util.List;

import jp.scid.bio.store.element.GeneticSequence;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.Factory;

public class BasicSequenceCollection extends AbstractMutableSequenceCollection {
    final static Field<Long> LAST_MODIFICATION = fieldByName(Long.class, "last_modification");
    final static Field<String> TABLE_NAME = fieldByName(String.class, "table_name");
    
    private final long folderId;
    
    public BasicSequenceCollection(Factory factory, long folderId) {
        super(factory);
        
        this.folderId = folderId;
    }
    
    @Override
    protected List<GeneticSequence> retrieve() {
        LinkedList<Field<?>> fields = new LinkedList<Field<?>>(Tables.GENETIC_SEQUENCE.getFields());
        fields.addFirst(Tables.COLLECTION_ITEM.ID);
        fields.addFirst(Tables.COLLECTION_ITEM.FOLDER_ID);
        
        Result<Record> result = create.select(fields)
                .from(Tables.COLLECTION_ITEM)
                .join(Tables.GENETIC_SEQUENCE)
                .on(Tables.COLLECTION_ITEM.GENETIC_SEQUENCE_ID.eq(Tables.GENETIC_SEQUENCE.ID))
                .orderBy(Tables.COLLECTION_ITEM.ID)
                .fetch();
        return result.map(GeneticSequence.getFolderContentMapper());
    }
    
    @Override
    public void addSequence(GeneticSequence sequence) {
        if (sequence == null) throw new IllegalArgumentException("sequence must not be null");
        
        sequence.store();
        
        CollectionItemRecord item = create.newRecord(Tables.COLLECTION_ITEM);
        item.setFolderId(folderId);
        item.setGeneticSequenceId(sequence.getId());
        item.store();
    }

    @Override
    public String getTableName() {
        return Tables.COLLECTION_ITEM.getName();
    }

    @Override
    protected boolean insertIntoStore(GeneticSequence element) {
        CollectionItemRecord item = create.newRecord(Tables.COLLECTION_ITEM);
        item.setFolderId(folderId);
        item.setGeneticSequenceId(element.getId());
        
        return item.store() > 0;
    }
}