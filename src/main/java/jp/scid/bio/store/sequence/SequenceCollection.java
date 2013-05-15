package jp.scid.bio.store.sequence;

import javax.swing.ListModel;

import jp.scid.bio.store.base.AbstractRecordListModel;

public interface SequenceCollection<E extends GeneticSequence> extends ListModel {
    @Override
    E getElementAt(int index);
    
    @Override
    public int getSize();
}

abstract class AbstractSequenceCollection<E extends GeneticSequence> extends AbstractRecordListModel<E>
        implements SequenceCollection<E> {
    AbstractSequenceCollection() {
    }
}
