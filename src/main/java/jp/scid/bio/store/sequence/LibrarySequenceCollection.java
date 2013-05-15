package jp.scid.bio.store.sequence;

import java.util.List;

import jp.scid.bio.store.GeneticSequenceParser;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.Result;
import org.jooq.impl.Factory;

public class LibrarySequenceCollection extends AbstractSequenceCollection<GeneticSequence> {
    private final Factory create;
    
    private LibrarySequenceCollection(Factory create) {
        this.create = create;
    }
    
    public static LibrarySequenceCollection fromFactory(Factory create, GeneticSequenceParser parser) {
        LibrarySequenceCollection collection = new LibrarySequenceCollection(create);
        return collection;
    }

    @Override
    protected List<GeneticSequence> retrieve() {
        Result<GeneticSequenceRecord> result = create.selectFrom(Tables.GENETIC_SEQUENCE)
                .orderBy(Tables.GENETIC_SEQUENCE.ID)
                .fetch();
        return result.map(GeneticSequenceRecordMapper.basicMapper());
    }

    void addSequence(DefaultGeneticSequence newRecord) {
        add(newRecord);
    }
}