package jp.scid.bio.store.folder;

import java.util.List;

import jp.scid.bio.store.base.RecordModel;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequenceSource;

/**
 * シーケンスデータを格納したフォルダの構造定義
 * @author higuchi
 *
 */
public interface Folder extends RecordModel, GeneticSequenceSource {
    /**
     * 親フォルダを返します。
     * @return 親フォルダ。ルートフォルダで親がいないときは {@code null} 。
     */
    FoldersContainer getParent();
    
    /**
     * このフォルダの名前を設定します。
     * 
     * @param newName 新しい名前
     */
    void setName(String newName);

    /**
     * 親からこのフォルダを除去します。
     */
    void deleteFromParent();
    
    /**
     * このフォルダにある配列情報を返します。
     * 
     * @return 配列情報
     */
    List<FolderContentGeneticSequence> getGeneticSequences();

}