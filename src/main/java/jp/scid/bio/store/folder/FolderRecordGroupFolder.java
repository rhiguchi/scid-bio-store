package jp.scid.bio.store.folder;

import java.util.List;

import javax.swing.event.ChangeListener;

import jp.scid.bio.store.SequenceLibrary.ChangeEventSupport;
import jp.scid.bio.store.base.AbstractRecordListModel;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;

public class FolderRecordGroupFolder extends AbstractFolder implements FoldersContainer {
    private final GroupFolderChildren childFolders;
    private final ChangeEventSupport folrdersChangeSupport;
    
    public FolderRecordGroupFolder(FolderRecord record, AbstractFolder.Source source) {
        super(record, source);
        
        childFolders = new GroupFolderChildren();
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
    
    public void addChildFolder(Folder folder) {
        folder.setParent(this);
        folrdersChangeSupport.fireStateChange();
    }
    
    private class GroupFolderChildren extends AbstractRecordListModel<Folder> implements FolderList {
        @Override
        protected List<Folder> retrieve() {
            return source.retrieveFolderChildren(FolderRecordGroupFolder.this, id());
        }
    }
}
