package jp.scid.bio.store.folder;

import java.util.List;

import jp.scid.bio.store.base.RecordModel;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequenceSource;

/**
 * シーケンスデータを格納したフォルダの構造定義
 * @author HIGUCHI Ryusuke
 *
 */
public interface Folder extends RecordModel, GeneticSequenceSource {
    /**
     * 親フォルダの ID を返します。
     * @return 親フォルダ。ルートフォルダで親がいないときは {@code null} 。
     */
    Long parentId();
    
    /**
     * 親フォルダの ID を設定します。
     * 
     * @param parentId フォルダ ID。ルートフォルダのときは {@code null}
     */
    void setParentId(Long parentId);
    
    /**
     * このフォルダの名前を設定します。
     * 
     * @param newName 新しい名前
     */
    void setName(String newName);

    /**
     * このフォルダのコンテンツとなる配列情報を返します。
     */
    List<FolderContentGeneticSequence> getGeneticSequences();
}