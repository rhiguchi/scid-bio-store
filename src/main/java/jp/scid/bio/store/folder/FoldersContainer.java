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
     * 子フォルダを削除します。
     * 
     * @param folder 削除するフォルダ。
     * @return フォルダがこのコンテナの持つフォルダで、削除ができたら true 。
     */
    boolean removeChildFolder(Folder folder);
    
    /**
     * 他の親のフォルダを追加します。
     * 
     * @param folder
     */
    void addChildFolder(Folder folder);
    
    /**
     * 子フォルダを返します
     * @return 子フォルダ
     */
    Iterable<Folder> getChildFolders();

    void addFoldersChangeListener(ChangeListener listener);
    
    void removeFoldersChangeListener(ChangeListener listener);
    
    /**
     * 新しいフォルダを追加します。
     * 
     * @param type フォルダの型
     * @return 追加されたフォルダ
     */
    Folder createChildFolder(CollectionType type);
}
