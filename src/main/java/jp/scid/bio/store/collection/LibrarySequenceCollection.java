package jp.scid.bio.store.collection;

import java.util.List;

import jp.scid.bio.store.element.GeneticSequence;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.Result;
import org.jooq.impl.Factory;

public class LibrarySequenceCollection extends AbstractMutableSequenceCollection {
    
    public LibrarySequenceCollection(Factory factory) {
        super(factory);
    }

    @Override
    protected List<GeneticSequence> retrieve() {
        Result<GeneticSequenceRecord> result = create.selectFrom(Tables.GENETIC_SEQUENCE)
                .fetch();
        return result.map(GeneticSequence.getDefaultMapper());
    }

    @Override
    public String getTableName() {
        return Tables.GENETIC_SEQUENCE.getName();
    }

    @Override
    protected boolean insertIntoStore(GeneticSequence element) {
        element.attach(create);
        element.setValue(Tables.GENETIC_SEQUENCE.ID, null);
        return element.store();
    }
    
    @Override
    void addSequence(GeneticSequence newRecord) {
        add(newRecord);
    }
}