package jp.scid.bio.store.folder;

import java.util.List;

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
    boolean addChildFolder(Folder folder);
    
    /**
     * 指定したフォルダを子フォルダとして追加できるかを返します
     * @param folder
     * @return フォルダが自分の親または先祖ではないとき {@code true} 。
     */
    boolean canAddChild(Folder folder);
    
    /**
     * 子フォルダを返します
     * @return 子フォルダ
     */
    List<Folder> getChildFolders();

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
