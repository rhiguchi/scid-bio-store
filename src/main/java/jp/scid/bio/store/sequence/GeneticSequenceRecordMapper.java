package jp.scid.bio.store.sequence;

import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.RecordMapper;

public class GeneticSequenceRecordMapper implements RecordMapper<GeneticSequenceRecord, GeneticSequence> {
    private final JooqGeneticSequence.Source geneticSequenceSource;
    
    public GeneticSequenceRecordMapper(JooqGeneticSequence.Source geneticSequenceSource) {
        if (geneticSequenceSource == null)
            throw new IllegalArgumentException("geneticSequenceSource must not be null");
        this.geneticSequenceSource = geneticSequenceSource;
    }
    
    @Override
    public GeneticSequence map(GeneticSequenceRecord record) {
        return new JooqGeneticSequence(record, geneticSequenceSource);
    }
}