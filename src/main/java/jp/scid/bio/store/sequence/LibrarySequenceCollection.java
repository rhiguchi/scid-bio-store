package jp.scid.bio.store.sequence;

import java.util.List;

import jp.scid.bio.store.GeneticSequenceParser;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.impl.Factory;

public class LibrarySequenceCollection extends AbstractMutableSequenceCollection<GeneticSequence> {
    private final Source source;
    
    LibrarySequenceCollection(Source source) {
        super(source);
        
        this.source = source;
    }
    
    public static LibrarySequenceCollection fromFactory(Factory create, GeneticSequenceParser parser) {
        JooqSource source = new JooqSource(create);
        source.setParser(parser);
        return new LibrarySequenceCollection(source);
    }

    @Override
    protected List<GeneticSequence> retrieve() {
        return source.retrieveAllSequences();
    }

    @Override
    void addSequence(GeneticSequence newRecord) {
        add(newRecord);
    }
    
    public static interface Source extends MutableSequenceCollection.Source {
        List<GeneticSequence> retrieveAllSequences();
    }
    
    private static class JooqSource extends AbstractMutableSequenceCollection.JooqSource
            implements Source, RecordMapper<GeneticSequenceRecord, GeneticSequence> {
        private final Factory create;

        public JooqSource(Factory create) {
            super();
            this.create = create;
        }
        
        public List<GeneticSequence> retrieveAllSequences() {
            Result<GeneticSequenceRecord> result = create.selectFrom(Tables.GENETIC_SEQUENCE)
                    .fetch();
            return result.map(this);
        }
        
        @Override
        public GeneticSequence map(GeneticSequenceRecord record) {
            return new GeneticSequenceImpl(record);
        }
    }
}