package jp.scid.bio.store.sequence;

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
    
    GeneticSequence(GeneticSequenceRecord record) {
        super(record);
    }

    public GeneticSequence() {
        this(null);
    }
    
    // persistence
    @Override
    public boolean delete() {
        return record.delete() > 0;
    }
    
    @Override
    public boolean save() {
        return record.store() > 0;
    }
    
    // id
    @Override
    public Long id() {
        return record.getId();
    }
    
    @Override
    protected void setId(Long id) {
        record.setId(id);
    }
    
    @Override
    protected GeneticSequenceRecord createRecord() {
        return new GeneticSequenceRecord();
    }

    public void loadFrom(File file, GeneticSequenceParser parser) throws IOException {
        // TODO Auto-generated method stub
    }
    
    GeneticSequenceRecord getRecord() {
        return super.record;
    }
}
