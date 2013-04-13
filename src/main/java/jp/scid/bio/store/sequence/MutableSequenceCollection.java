package jp.scid.bio.store.sequence;

import java.io.File;
import java.io.IOException;

import jp.scid.bio.store.GeneticSequenceParser;

public interface MutableSequenceCollection<E extends GeneticSequence> extends SequenceCollection<E>  {

    public static interface Source {
        void loadSequenceFileInto(GeneticSequence sequence, File file) throws IOException;
    }
}

abstract class AbstractMutableSequenceCollection<E extends GeneticSequence>
        extends AbstractSequenceCollection<E> implements MutableSequenceCollection<E> {
    private final Source source;
    
    AbstractMutableSequenceCollection(Source source) {
        if (source == null) throw new IllegalArgumentException("source must not be null");
        this.source = source;
    }

    public GeneticSequence addElementFromFile(File file) throws IOException {
        GeneticSequence sequence = new GeneticSequenceImpl();
        source.loadSequenceFileInto(sequence, file);
        addSequence(sequence);
        return sequence;
    }
    
    abstract void addSequence(GeneticSequence newRecord);
    
    static class JooqSource implements Source {
        private GeneticSequenceParser parser = null;
        
        GeneticSequenceParser parser() {
            if (parser == null) throw new IllegalStateException("parser must not be null");
            
            return parser;
        }
        
        public void setParser(GeneticSequenceParser parser) {
            this.parser = parser;
        }
        
        @Override
        public void loadSequenceFileInto(GeneticSequence sequence, File file) throws IOException {
            sequence.loadFrom(file, parser());
            sequence.save();
        }
    }
}