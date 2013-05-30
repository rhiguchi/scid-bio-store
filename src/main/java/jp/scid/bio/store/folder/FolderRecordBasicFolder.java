package jp.scid.bio.store.folder;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import jp.scid.bio.store.folder.FolderRecordGroupFolder.Source;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;

public class FolderRecordBasicFolder extends AbstractFolder {
    public FolderRecordBasicFolder(FolderRecord record, Source folderSource) {
        super(record, folderSource);
    }

    public FolderContentGeneticSequence addSequence(GeneticSequence sequence) {
        FolderContentGeneticSequence content = source.createFolderContent(sequence, this);
        content.save();
        sequences.add(content);
        return content;
    }
    
    public FolderContentGeneticSequence importSequence(File file) throws IOException, ParseException {
        GeneticSequence sequence = source.createGeneticSequence(file);
        sequence.save();
        return addSequence(sequence);
    }
    
    public FolderContentGeneticSequence removeSequenceAt(int index) {
        FolderContentGeneticSequence sequence = sequences.removeElementAt(index);
        sequence.delete();
        return sequence;
    }
}