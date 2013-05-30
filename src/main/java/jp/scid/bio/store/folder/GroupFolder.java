package jp.scid.bio.store.folder;

/**
 * 子フォルダをもつフォルダ
 * @author higuchi
 *
 */
public interface GroupFolder extends Folder, FoldersContainer {
    void addChild(Folder folder);
    
    void moveChildTo(int childIndex, GroupFolder dest);
}