package jp.scid.bio.store.collection;

import java.util.List;

import javax.swing.ListModel;

import jp.scid.bio.store.element.CollectionType;
import jp.scid.bio.store.element.SequenceFolder;


public interface SequenceFolderList extends ListModel {
    @Override
    public SequenceFolder getElementAt(int index);
    
    /**
     * 指定した位置にあるフォルダを削除します
     * 
     * @param index フォルダの位置
     * @return 削除されたフォルダ
     */
    public SequenceFolder remove(int index);
    
    /**
     * このフォルダを親にした新しいフォルダを追加します。
     * 
     * @return 追加されたフォルダ
     */
    public SequenceFolder add(CollectionType type);
    
    /**
     * 
     * @param list
     * @param index
     */
    public void moveChildFrom(SequenceFolderList list, int index);
    

    public static interface Source {
        List<SequenceFolder> findChildFolders(Long parentFolderId);

        String getNewFolderName(CollectionType type);
    }
}