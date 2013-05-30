package jp.scid.bio.store.folder;

import jp.scid.bio.store.base.RecordModel;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.SequenceCollection;

/**
 * シーケンスデータを格納したフォルダの構造定義
 * @author higuchi
 *
 */
public interface Folder extends RecordModel {
    /**
     * 親フォルダを返します。
     * @return 親フォルダ。ルートフォルダで親がいないときは {@code null} 。
     */
    GroupFolder getParent();
    
    /**
     * 親フォルダの id を設定します。
     * @param newParentId 新しい親のフォルダ id。親のいないルートフォルダにするときは {@code null} 。
     */
    void setParentId(Long newParentId);
    
    /**
     * このフォルダの名前を設定します。
     * 
     * @param newName 新しい名前
     */
    void setName(String newName);

    /**
     * このフォルダにある配列情報を返します。
     * 
     * @return 配列情報
     */
    SequenceCollection<FolderContentGeneticSequence> getContentSequences();
}