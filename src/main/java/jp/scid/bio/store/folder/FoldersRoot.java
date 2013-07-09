package jp.scid.bio.store.folder;

import javax.swing.event.ChangeListener;

import jp.scid.bio.store.base.ChangeEventSupport;

public class FoldersRoot implements FoldersContainer {
    private final AbstractFolder.Source folderSource;
    private final ChangeEventSupport folrdersChangeSupport;
    
    public FoldersRoot(AbstractFolder.Source folderSource) {
        this.folderSource = folderSource;
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
    public Iterable<Folder> getChildFolders() {
        return folderSource.retrieveFolderChildren(this, null);
    }
    
    @Override
    public Folder createChildFolder(CollectionType type) {
        return folderSource.createFolder(type, null, this);
    }
    
    @Override
    public boolean removeChildFolder(Folder folder) {
        if (this.equals(folder.getParent())) {
            return false;
        }
        
        boolean result = folder.delete();
        if (result) {
            folrdersChangeSupport.fireStateChange();
        }
        
        return result;
    }

    @Override
    public void addChildFolder(Folder folder) {
        folder.setParent(this);
        folrdersChangeSupport.fireStateChange();
    }
    
    @Override
    public String toString() {
        return "User Collections";
    }
}