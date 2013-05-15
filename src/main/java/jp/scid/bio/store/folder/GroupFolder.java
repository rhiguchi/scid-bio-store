package jp.scid.bio.store.folder;

/**
 * 子フォルダをもつフォルダ
 * @author higuchi
 *
 */
public interface GroupFolder extends Folder {
    void moveChildFrom(GroupFolder list, int index);
    
    Folder removeChild(int index);
    
    FolderList getChildFolders();
}