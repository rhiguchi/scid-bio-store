package jp.scid.bio.store;

import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

public class GeneticSequence extends JooqRecordHolder<GeneticSequenceRecord> {
    
    static GeneticSequence newFolderContent(long folderId, long contentId, GeneticSequenceRecord record) {
        return new FolderContentGeneticSequence();
    }
    
    public GeneticSequence() {
        super();
    }
    
    public long getLookupValue() {
        return 0; // TODO;
    }
    
    public boolean delete() {
        return record.delete() > 0;
    }
    
    public boolean store() {
        return record.store() > 0;
    }
    
    @Override
    public long getId() {
        return record.getId();
    }
    
    @Override
    protected GeneticSequenceRecord createRecord() {
        return new GeneticSequenceRecord();
    }
    
    public static RecordMapper<GeneticSequenceRecord, GeneticSequence> getDefaultMapper() {
        return null; //TODO
    }
    
    public static RecordMapper<Record, GeneticSequence> getFolderContentMapper() {
        return null; //TODO
    }
    
    static class GeneticSequenceRecordMapper implements RecordMapper<Record, GeneticSequence> {
        private final Field<Long> idField;
        
        public GeneticSequenceRecordMapper(Field<Long> idField) {
            this.idField = idField;
        }
        
        @Override
        public GeneticSequence map(Record record) {
            Long id = record.getValue(idField);
            GeneticSequenceRecord gsRecord = record.into(Tables.GENETIC_SEQUENCE);
            return newFolderContent(0, id, gsRecord);
        }
    }
}

class FolderContentGeneticSequence extends GeneticSequence {
    
}

abstract class JooqRecordHolder<R extends Record> {
    protected final R record;

    public JooqRecordHolder() {
        this(null);
    }
    
    public JooqRecordHolder(R record) {
        if (record == null) {
            record = createRecord();
        }
        
        this.record = record;
    }
    
    abstract protected R createRecord();

    public abstract long getId();
    
    public void attach(Configuration configuration) {
        record.attach(configuration);
    }
    
    public <T> T getValue(Field<T> field) throws IllegalArgumentException {
        return record.getValue(field);
    }

    public <T> void setValue(Field<T> field, T value) {
        record.setValue(field, value);
    }

    public boolean changed() {
        return record.changed();
    }
}