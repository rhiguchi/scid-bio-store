package jp.scid.bio.store.folder;

import javax.swing.ListModel;

public interface FolderList extends ListModel {
    @Override
    Folder getElementAt(int index);
    
    @Override
    public int getSize();
    
    boolean removeElement(Folder folder);
    
    void addElement(Folder folder);
}
