package jp.scid.bio.store.base;

import java.beans.PropertyChangeListener;

import org.jooq.Field;

/**
 * 永続化されるモデルの構造です
 * @author higuchi
 *
 */
public interface RecordModel {
    /**
     * このモデルの id を返します。{@code null} であることもあります。
     * 
     * @return id 値。
     */
    Long id();
    
    /**
     * このモデルを、保存元から削除します。
     * 
     * @return 削除に成功したときは {@code true} 。
     *         まだ保存されていないとき、または既に削除されていたときは {@code false}
     */
    boolean delete();
    
    /**
     * このモデルを保存します。
     * 
     * @return 保存に成功したときは {@code true} 。
     *         モデルに変更がなく、保存してもデータの更新がなかったときは {@code false} 。
     */
    boolean save();
    
    /**
     * このモデルが最後に保存されたときから変更されているかを返します。
     * 
     * @return 変更されているときは {@code true} 。
     */
    boolean changed();
    
    /**
     * プロパティ値を返します。
     * 
     * @param field プロパティ値のフィールド
     * @return プロパティ値
     * @throws IllegalArgumentException field がこのモデルのプロパティの値ではないとき
     */
    <T> T getValue(Field<T> field) throws IllegalArgumentException;
    
    /**
     * プロパティ値を設定します。
     * 
     * @param field プロパティ値のフィールド
     * @param value 設定するプロパティ値
     * @throws IllegalArgumentException field がこのモデルのプロパティの値ではないとき
     */
    <T> void setValue(Field<T> field, T value) throws IllegalArgumentException;
    
    void addPropertyChangeListener(PropertyChangeListener listener);
    
    void removePropertyChangeListener(PropertyChangeListener listener);
}