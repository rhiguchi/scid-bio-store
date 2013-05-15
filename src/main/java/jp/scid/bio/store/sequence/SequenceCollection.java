package jp.scid.bio.store.sequence;

import jp.scid.bio.store.base.AbstractRecordListModel;
import jp.scid.bio.store.base.RecordListModel;

public interface SequenceCollection<E extends GeneticSequence> extends RecordListModel<E> {
    @Override
    E getElementAt(int index);
    
    @Override
    public int getSize();
    
    @Override
    @Deprecated
    public void fetch();
}

abstract class AbstractSequenceCollection<E extends GeneticSequence> extends AbstractRecordListModel<E>
        implements SequenceCollection<E> {
    AbstractSequenceCollection() {
    }
}
