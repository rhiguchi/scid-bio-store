package jp.scid.bio.store.element;

import java.io.File;
import java.io.IOException;

import jp.scid.bio.store.GeneticSequenceParser;
import jp.scid.bio.store.base.RecordModel;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

public class GeneticSequence extends RecordModel<GeneticSequenceRecord> {
    
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
    
    public Long getId() {
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

    public void loadFrom(File file, GeneticSequenceParser parser) throws IOException {
        // TODO Auto-generated method stub
        
    }
}

class FolderContentGeneticSequence extends GeneticSequence {
    
}