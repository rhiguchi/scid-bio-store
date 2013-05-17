package jp.scid.bio.store.folder;

/**
 * 子フォルダをもつフォルダ
 * @author higuchi
 *
 */
public interface GroupFolder extends Folder {
    Folder addChild(CollectionType type);
    
    void moveChildFrom(GroupFolder list, int index);
    
    Folder removeChild(int index);
    
    FolderList getChildFolders();
}