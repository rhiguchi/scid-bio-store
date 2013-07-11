package jp.scid.bio.store.folder;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;
import jp.scid.bio.store.sequence.ImportableSequenceSource;

public class FolderRecordBasicFolder extends AbstractFolder implements ImportableSequenceSource {
    public FolderRecordBasicFolder(FolderRecord record, AbstractFolder.Source folderSource) {
        super(record, folderSource);
    }

    public FolderContentGeneticSequence createContent(GeneticSequence sequence) {
        return source.createFolderContent(sequence, this);
    }
    
    public FolderContentGeneticSequence addSequence(GeneticSequence sequence) {
        FolderContentGeneticSequence content = createContents(sequence);
        content.save();
        
        fireSequencesChange();
        return content;
    }
    
    public List<FolderContentGeneticSequence> addAllSequences(Collection<? extends GeneticSequence> sequences) {
        List<FolderContentGeneticSequence> list = new ArrayList<FolderContentGeneticSequence>(sequences.size());
        
        for (GeneticSequence s: sequences) {
            FolderContentGeneticSequence content = createContents(s);
            content.save();
            
            list.add(content);
        }
        
        fireSequencesChange();
        return list;
    }
    
    public FolderContentGeneticSequence importSequence(File file) throws IOException, ParseException {
        GeneticSequence sequence = source.createGeneticSequence(file);
        FolderContentGeneticSequence content = createContents(sequence);
        content.save();
        
        fireSequencesChange();
        return content;
    }
    
    private FolderContentGeneticSequence createContents(GeneticSequence sequence) {
        return source.createFolderContent(sequence, this);
    }
    
    public boolean removeSequence(FolderContentGeneticSequence sequence) {
        if (sequence.folderId() == null || !id().equals(sequence.folderId())) {
            return false;
        }
        
        boolean result = sequence.delete();
        if (result) {
            fireSequencesChange();
        }
        return result;
    }
}