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
    FolderList getContentFolders();
    
    /**
     * 子フォルダを追加します
     * 
     * @param type フォルダの型
     * @return 追加されたフォルダ
     */
    Folder createContentFolder(CollectionType type);
    
    /**
     * 子フォルダを削除します
     * 
     * @param index フォルダの順序
     * @return 削除されたフォルダ
     */
    Folder removeContentFolderAt(int index);
    
    int indexOfFolder(Folder folder);
    
    boolean removeContentFolder(Folder folder);
    
    void addContentFolder(Folder folder);
}
