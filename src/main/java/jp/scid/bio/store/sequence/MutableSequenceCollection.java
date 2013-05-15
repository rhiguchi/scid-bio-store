package jp.scid.bio.store.sequence;

import java.io.File;
import java.io.IOException;

import jp.scid.bio.store.GeneticSequenceParser;

@Deprecated
public interface MutableSequenceCollection<E extends GeneticSequence> extends SequenceCollection<E>  {

    E remove(int index);
    
    void add(E element);
    
    void add(int index, E element);
    
    public static interface Source {
        void loadSequenceFileInto(GeneticSequence sequence, File file) throws IOException;
    }
}

@Deprecated
abstract class AbstractMutableSequenceCollection<E extends GeneticSequence>
        extends AbstractSequenceCollection<E> implements SequenceCollection<E> {
    private GeneticSequenceParser parser = null;
    
    protected AbstractMutableSequenceCollection() {
    }

    public boolean canImport(File file) {
        if (parser == null) {
            return false;
        }
        return true;    
    }
    
//    public GeneticSequence importSequence(File file) throws IOException {
//        DefaultGeneticSequence sequence = new DefaultGeneticSequence();
//        loadSequence(file, sequence);
//        addSequence(sequence);
//        return sequence;
//    }

//    private void loadSequence(File file, DefaultGeneticSequence sequence) throws IOException {
//        if (parser == null) {
//            throw new IllegalStateException("cannot import file because parser doesn't exist");
//        }
//        sequence.loadFrom(file, parser);
//        sequence.save();
//    }
//    
//    abstract void addSequence(DefaultGeneticSequence newRecord);
    
    public GeneticSequenceParser getParser() {
        return parser;
    }
    
    public void setParser(GeneticSequenceParser parser) {
        this.parser = parser;
    }
}