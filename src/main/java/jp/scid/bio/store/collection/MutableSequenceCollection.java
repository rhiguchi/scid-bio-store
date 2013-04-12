package jp.scid.bio.store.collection;

import java.io.File;
import java.io.IOException;

import jp.scid.bio.store.element.GeneticSequence;

public interface MutableSequenceCollection extends SequenceCollection {
    /**
     * 
     * @param file
     * @return new index
     * @throws IOException
     */
    public GeneticSequence addElementFromFile(File file) throws IOException;
}