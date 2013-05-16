package jp.scid.bio.store.sequence;

import java.io.File;
import java.io.IOException;

import jp.scid.bio.store.GeneticSequenceParser;
import jp.scid.bio.store.base.AbstractRecordModel;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

public class DefaultGeneticSequence extends AbstractRecordModel<GeneticSequenceRecord> implements GeneticSequence {
    
    public DefaultGeneticSequence(GeneticSequenceRecord record) {
        super(record);
    }

    public DefaultGeneticSequence() {
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
    
    public GeneticSequenceRecord getRecord() {
        return super.record;
    }
}