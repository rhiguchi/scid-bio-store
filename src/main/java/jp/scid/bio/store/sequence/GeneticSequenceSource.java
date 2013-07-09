package jp.scid.bio.store.sequence;

import javax.swing.event.ChangeListener;

/**
 * 配列情報を持っている情報モデルです。
 * 
 * @author higuchi
 */
public interface GeneticSequenceSource {
    /**
     * 配列情報をリストで返します。
     * 
     * @return 配列情報のリスト
     */
    Iterable<? extends GeneticSequence> getGeneticSequences();
    
    void addSequencesChangeListener(ChangeListener listener);
    
    void removeSequencesChangeListener(ChangeListener listener);
}

