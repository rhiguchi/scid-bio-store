package jp.scid.bio.store.folder;

import java.util.List;

import jp.scid.bio.store.base.RecordListModel;

public interface FolderList extends RecordListModel<Folder> {
    long folderId();
    
    Folder remove(int index);
    
    void moveChildFrom(FolderList list, int index);
    
    public static interface Source {
        List<Folder> findChildFolders(Long parentFolderId);

        String getNewFolderName(CollectionType type);
        
        boolean isDescend(Long folderId, Long folderId2);
    }
}