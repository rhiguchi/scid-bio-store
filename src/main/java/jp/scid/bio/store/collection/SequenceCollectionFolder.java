package jp.scid.bio.store.collection;

import java.util.Collections;
import java.util.List;

import jp.scid.bio.store.SequenceCollectionList;
import jp.scid.bio.store.collection.AbstractSequenceCollection.IdentifiableRecord;

import org.jooq.impl.Factory;

public interface SequenceCollectionFolder extends SequenceCollection {
    
    SequenceCollectionList fetchChildCollections();
}

class NodeSequenceCollection extends AbstractFolderSequenceCollection implements SequenceCollectionFolder {

    public NodeSequenceCollection(Factory factory, long folderId) {
        super(factory, folderId);
    }

    @Override
    protected List<IdentifiableRecord> retrieve() {
        // TODO Auto-generated method stub
        return Collections.emptyList();
    }

    @Override
    public SequenceCollectionList fetchChildCollections() {
        // TODO Auto-generated method stub
        return null;
    }
    
}