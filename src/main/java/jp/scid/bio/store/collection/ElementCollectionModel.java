package jp.scid.bio.store.collection;

import javax.swing.ListModel;

/**
 * コレクションの基底インターフェイス
 *
 * @param <E> モデルのクラス
 */
interface ElementCollectionModel<E> extends ListModel {
    public void fetch();
    
    public E getElementAt(int index);
}
