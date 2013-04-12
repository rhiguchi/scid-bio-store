package jp.scid.bio.store.collection;

import static org.jooq.impl.Factory.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import jp.scid.bio.store.GeneticSequence;
import jp.scid.bio.store.JooqTableContents;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.Factory;

public abstract class AbstractSequenceCollection<E extends GeneticSequence> extends JooqTableContents<E> implements SequenceCollection {
    private final LastModificationCheck modificationCheck;
    
    public AbstractSequenceCollection(Factory factory) {
        super(factory);
        modificationCheck = new LastModificationCheck(factory, Tables.GENETIC_SEQUENCE.getName());
    }

    public GeneticSequenceRecord addFile(File file) throws IOException {
        GeneticSequenceRecord newRecord = create.newRecord(Tables.GENETIC_SEQUENCE);
        newRecord.setId(null);
        newRecord.setName("Untitiled");
        
//        addRecord(newRecord);
        
        return newRecord;
    }

    @Override
    protected boolean deleteFromStore(GeneticSequence element) {
        return element.delete();
    }
    
    @Override
    protected boolean update(GeneticSequence element) {
        return element.store();
    }
    
    @Override
    protected Long getId(GeneticSequence element) {
        return element.getLookupValue();
    }
}

class LibraryDelegate extends AbstractSequenceCollection<GeneticSequence> {
    
    public LibraryDelegate(Factory factory) {
        super(factory);
    }

    @Override
    protected List<GeneticSequence> retrieve() {
        Result<GeneticSequenceRecord> result = create.selectFrom(Tables.GENETIC_SEQUENCE)
                .fetch();
        return result.map(GeneticSequence.getDefaultMapper());
    }

    @Override
    public String getTableName() {
        return Tables.GENETIC_SEQUENCE.getName();
    }

    @Override
    protected boolean insertIntoStore(GeneticSequence element) {
        element.attach(create);
        element.setValue(Tables.GENETIC_SEQUENCE.ID, null);
        return element.store();
    }
}

class BasicSequenceCollection extends AbstractSequenceCollection<GeneticSequence> {
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
    
    public void addRecord(GeneticSequenceRecord record) {
        if (record == null) throw new IllegalArgumentException("record must not be null");
        
        record.store();
        
        CollectionItemRecord item = create.newRecord(Tables.COLLECTION_ITEM);
        item.setFolderId(folderId);
        item.setGeneticSequenceId(record);
        item.store();

        Long lookupId = item.getId();
        
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
