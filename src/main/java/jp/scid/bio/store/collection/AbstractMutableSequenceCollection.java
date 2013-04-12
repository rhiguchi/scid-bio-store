package jp.scid.bio.store.collection;

import java.io.File;
import java.io.IOException;

import jp.scid.bio.store.GeneticSequenceParser;
import jp.scid.bio.store.element.GeneticSequence;

import org.jooq.impl.Factory;

abstract class AbstractMutableSequenceCollection extends AbstractSequenceCollection<GeneticSequence> implements MutableSequenceCollection {
    private GeneticSequenceParser parser;
    
    public AbstractMutableSequenceCollection(Factory factory) {
        super(factory);
    }

    public GeneticSequence addElementFromFile(File file) throws IOException {
        GeneticSequence sequence = new GeneticSequence();
        sequence.loadFrom(file, parser);
        addSequence(sequence);
        return sequence;
    }
    
    abstract void addSequence(GeneticSequence newRecord);
}