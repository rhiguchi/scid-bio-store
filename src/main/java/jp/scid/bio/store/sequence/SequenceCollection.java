package jp.scid.bio.store.sequence;

import javax.swing.ListModel;

@Deprecated
public interface SequenceCollection<E extends GeneticSequence> extends ListModel {
    @Override
    E getElementAt(int index);
    
    @Override
    public int getSize();
}
