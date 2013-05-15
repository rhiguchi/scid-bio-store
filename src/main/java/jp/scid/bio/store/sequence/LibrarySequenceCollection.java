package jp.scid.bio.store.sequence;

import java.util.List;

import jp.scid.bio.store.GeneticSequenceParser;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.impl.Factory;

public class LibrarySequenceCollection extends AbstractMutableSequenceCollection<GeneticSequence> {
    private final Factory create;
    private final Mapper mapper;
    
    private LibrarySequenceCollection(Factory create) {
        this.create = create;
        mapper = new Mapper();
    }
    
    public static LibrarySequenceCollection fromFactory(Factory create, GeneticSequenceParser parser) {
        LibrarySequenceCollection collection = new LibrarySequenceCollection(create);
        collection.setParser(parser);
        return collection;
    }

    @Override
    protected List<GeneticSequence> retrieve() {
        Result<GeneticSequenceRecord> result = create.selectFrom(Tables.GENETIC_SEQUENCE)
                .orderBy(Tables.GENETIC_SEQUENCE.ID)
                .fetch();
        return result.map(mapper);
    }

    void addSequence(DefaultGeneticSequence newRecord) {
        add(newRecord);
    }
    
    public static interface Source extends MutableSequenceCollection.Source {
        List<GeneticSequence> retrieveAllSequences();
    }
    
    private static class Mapper implements RecordMapper<GeneticSequenceRecord, GeneticSequence> {
        @Override
        public GeneticSequence map(GeneticSequenceRecord record) {
            return new DefaultGeneticSequence(record);
        }
    }
}