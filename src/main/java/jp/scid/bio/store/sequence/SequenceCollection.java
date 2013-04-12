package jp.scid.bio.store.sequence;

import jp.scid.bio.store.base.RecordListModel;

public abstract class SequenceCollection<E extends GeneticSequence> extends RecordListModel<E> {
    SequenceCollection() {
    }
    
    public static SequenceCollection<FolderContentGeneticSequence> newBasicFolderContents() {
        return null; //TODO
    }
}
