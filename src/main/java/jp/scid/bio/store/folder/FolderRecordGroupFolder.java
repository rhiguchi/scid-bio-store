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
        return source.createFolder(type, id(), this);
    }
    
    public Folder createContentFolder(CollectionType type) {
        Folder folder = source.createFolder(type, id(), this);
        folder.save();
        return folder;
    }
    
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
    
    public boolean addChildFolder(Folder folder) {
        folder.setParent(this);
        folrdersChangeSupport.fireStateChange();
        return true;
    }

    public boolean canAddChild(Folder folder) {
        // TODO Auto-generated method stub
        return false;
    }
}
