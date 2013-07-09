package jp.scid.bio.store.folder;

import javax.swing.event.ChangeListener;

/**
 * フォルダを内包しているオブジェクト構造
 * 
 * @author higuchi
 *
 */
public interface FoldersContainer {
    /**
     * 子フォルダを追加します
     * 
     * @param type フォルダの型
     * @return 追加されたフォルダ
     */
//    @Deprecated
//    Folder createContentFolder(CollectionType type);
    
    /**
     * 子フォルダを削除します
     * 
     * @param index フォルダの順序
     * @return 削除されたフォルダ
     */
//    @Deprecated
//    Folder removeContentFolderAt(int index);
//    
//    @Deprecated
//    int indexOfFolder(Folder folder);
//    
    boolean removeChildFolder(Folder folder);
    
    void addChildFolder(Folder folder);
    
    /**
     * 子フォルダを返します
     * @return 子フォルダ
     */
    Iterable<Folder> getChildFolders();

    void addFoldersChangeListener(ChangeListener listener);
    
    void removeFoldersChangeListener(ChangeListener listener);
    
    Folder createChildFolder(CollectionType type);
}
