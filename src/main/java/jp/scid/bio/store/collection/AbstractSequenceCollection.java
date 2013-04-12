package jp.scid.bio.store.collection;

import static org.jooq.impl.Factory.*;


import jp.scid.bio.store.element.GeneticSequence;

import org.jooq.impl.Factory;

public abstract class AbstractSequenceCollection<E extends GeneticSequence> extends JooqTableContents<E> implements SequenceCollection {
    
    public AbstractSequenceCollection(Factory factory) {
        super(factory);
    }


    @Override
    protected boolean deleteFromStore(GeneticSequence element) {
        return element.delete();
    }
    
    @Override
    protected boolean update(GeneticSequence element) {
        return element.store();
    }
    
    @Override
    protected Long getId(GeneticSequence element) {
        return element.getLookupValue();
    }
}
