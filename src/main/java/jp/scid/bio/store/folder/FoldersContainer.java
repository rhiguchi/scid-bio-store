package jp.scid.bio.store.folder;

/**
 * フォルダを内包しているオブジェクト構造
 * 
 * @author higuchi
 *
 */
public interface FoldersContainer {
    /**
     * 子フォルダのリストモデルを返します
     * 
     * @return 子フォルダのリスト
     */
    FolderList getChildFolders();
    
    /**
     * 子フォルダを追加します
     * 
     * @param type フォルダの型
     * @return 追加されたフォルダ
     */
    Folder addChildFolder(CollectionType type);
    
    /**
     * 子フォルダを削除します
     * @param child
     */
//    void removeChildFolder(Folder child);
}
