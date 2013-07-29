package jp.scid.bio.store.folder;

import java.util.List;

import javax.swing.event.ChangeListener;

import jp.scid.bio.store.base.ChangeEventSupport;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;

public class FolderRecordGroupFolder extends AbstractFolder implements FoldersContainer {
    private final ChangeEventSupport folrdersChangeSupport;
    
    public FolderRecordGroupFolder(FolderRecord record, AbstractFolder.Source source) {
        super(record, source);
        
        folrdersChangeSupport = new ChangeEventSupport(this);
    }

    @Override
    public void addFoldersChangeListener(ChangeListener listener) {
        folrdersChangeSupport.addChangeListener(listener);
    }
    
    @Override
    public void removeFoldersChangeListener(ChangeListener listener) {
        folrdersChangeSupport.removeChangeListener(listener);
    }
    
    @Override
    public List<Folder> getChildFolders() {
        return source.retrieveFolderChildren(this, id());
    }
    
    public Folder createChildFolder(CollectionType type) {
        Folder folder = source.createFolder(type, id(), this);
        folder.save();
        folrdersChangeSupport.fireStateChange();
        return folder;
    }
    
    public boolean removeChildFolder(Folder folder) {
        boolean result = false;
        
        if (id().equals(folder.parentId())) {
            result = folder.delete();
        }
        folrdersChangeSupport.fireStateChange();

        return result;
    }
    
    public boolean addChildFolder(Folder folder) {
        if (id() == null) {
            throw new IllegalStateException("parent folder must be saved");
        }
        else if (!canAddChild(folder)) {
            throw new IllegalArgumentException("reclusive path");
        }
        
        folder.setParentId(id());
        
        try {
            return folder.save();
        }
        finally {
            folrdersChangeSupport.fireStateChange();
        }
    }

    public boolean canAddChild(Folder folder) {
        Long childId = folder.id();
        if (childId == null) {
            throw new IllegalArgumentException("id of folder must be specified");
        }
        
        List<Long> idPath = source.getIdPathToRoot(id());
        
        return !idPath.contains(childId);
    }
}
