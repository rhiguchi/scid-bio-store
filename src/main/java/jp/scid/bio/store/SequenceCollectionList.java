package jp.scid.bio.store;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import jp.scid.bio.store.collection.SequenceCollection;

public interface SequenceCollectionList extends ListModel {
    @Override
    public SequenceCollection getElementAt(int index);
}

abstract class AbstractSequenceCollectionList extends AbstractListModel implements SequenceCollectionList {
    private final SequenceLibrary library;
    private final Long parentFolderId;

    private final List<SequenceCollection> elements;
    
    public AbstractSequenceCollectionList(SequenceLibrary library, Long parentFolderId) {
        this.library = library;
        this.parentFolderId = parentFolderId;
        
        elements = new ArrayList<SequenceCollection>();
    }

    public void fetch() {
        List<SequenceCollection> children = library.fetchCollections(parentFolderId);
        // TODO sync
    }

    @Override
    public int getSize() {
        return elements.size();
    }
    
    @Override
    public SequenceCollection getElementAt(int index) {
        return elements.get(index);
    }
}

class DefaultSequenceCollectionList extends AbstractSequenceCollectionList {
    
    public DefaultSequenceCollectionList(SequenceLibrary library, Long parentFolderId) {
        super(library, parentFolderId);
    }
}