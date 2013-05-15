package jp.scid.bio.store.sequence;


public interface FolderContentGeneticSequence extends GeneticSequence {
    Long folderId();
    
    Long sequenceId();
}
