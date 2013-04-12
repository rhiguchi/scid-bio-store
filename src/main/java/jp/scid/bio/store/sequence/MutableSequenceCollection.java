package jp.scid.bio.store.sequence;

import java.io.File;
import java.io.IOException;

import jp.scid.bio.store.GeneticSequenceParser;

public abstract class MutableSequenceCollection<E extends GeneticSequence> extends SequenceCollection<E>  {
    private final Source source;
    
    MutableSequenceCollection(Source source) {
        if (source == null) throw new IllegalArgumentException("source must not be null");
        this.source = source;
    }

    public GeneticSequence addElementFromFile(File file) throws IOException {
        GeneticSequence sequence = new GeneticSequence();
        source.loadSequenceFileInto(sequence, file);
        addSequence(sequence);
        return sequence;
    }
    
    abstract void addSequence(GeneticSequence newRecord);
    
    public static interface Source {
        void loadSequenceFileInto(GeneticSequence sequence, File file) throws IOException;
    }
    
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