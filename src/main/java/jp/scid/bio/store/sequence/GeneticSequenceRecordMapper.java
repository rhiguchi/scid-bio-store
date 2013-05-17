package jp.scid.bio.store.sequence;

import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.RecordMapper;

public class GeneticSequenceRecordMapper implements RecordMapper<GeneticSequenceRecord, GeneticSequence> {
    private final static GeneticSequenceRecordMapper singleton =
            new GeneticSequenceRecordMapper();
    
    private GeneticSequenceRecordMapper() {
    }
    
    public static GeneticSequenceRecordMapper basicMapper() {
        return singleton;
    }
    
    @Override
    public GeneticSequence map(GeneticSequenceRecord record) {
        return new JooqGeneticSequence(record);
    }
}