package jp.scid.bio.store.folder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;

public interface BasicSequenceFolder {
    List<FolderContentGeneticSequence> retrieveFolderContent();
    
    FolderContentGeneticSequence addSequence(GeneticSequence sequence);
    
    FolderContentGeneticSequence addSequence(File file) throws IOException;
}
