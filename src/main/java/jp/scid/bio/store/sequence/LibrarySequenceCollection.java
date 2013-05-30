package jp.scid.bio.store.sequence;

import java.util.List;

import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.Result;
import org.jooq.impl.Factory;

public class LibrarySequenceCollection extends AbstractSequenceCollection<GeneticSequence> {
    private final Factory create;
    private final GeneticSequenceRecordMapper mapper;
    
    public LibrarySequenceCollection(JooqGeneticSequence.Source geneticSequenceSource, Factory create) {
        if (create == null) throw new IllegalArgumentException("create must not be null");
        
        this.create = create;
        mapper = new GeneticSequenceRecordMapper(geneticSequenceSource);
    }
    
    @Override
    protected List<GeneticSequence> retrieve() {
        Result<GeneticSequenceRecord> result = create.selectFrom(Tables.GENETIC_SEQUENCE)
                .orderBy(Tables.GENETIC_SEQUENCE.ID)
                .fetch();
        return result.map(mapper);
    }
}