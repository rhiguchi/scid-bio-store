package jp.scid.bio.store.folder;

import java.io.File;
import java.io.IOException;

import jp.scid.bio.store.GeneticSequenceParser;
import jp.scid.bio.store.folder.FolderRecordGroupFolder.Source;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;
import jp.scid.bio.store.sequence.JooqGeneticSequence;

public class FolderRecordBasicFolder extends AbstractFolder {
    private GeneticSequenceParser parser = null;
    
    public FolderRecordBasicFolder(FolderRecord record, Source folderSource) {
        super(record, folderSource);
    }
    
    public FolderContentGeneticSequence addSequence(GeneticSequence sequence) {
        if (sequence.id() == null) {
            throw new IllegalStateException("id of sequence must not be null");
        }
        long folderId = id();
        
        CollectionItemRecord item = create().newRecord(Tables.COLLECTION_ITEM);
        item.setFolderId(folderId);
        item.setGeneticSequenceId(sequence.id());
        item.store();
        
        DefaultFolderContentGeneticSequence content =
                new DefaultFolderContentGeneticSequence(sequence, item);
        
        content.save();
        return content;
    }

    /**
     * 
     * @param file file to check
     * @return true for importing allowed
     */
    public boolean canImport(File file) {
        return parser != null;
    }
    
    public FolderContentGeneticSequence importSequence(File file) throws IOException {
        if (parser == null) {
            throw new IllegalStateException("cannot import file because parser doesn't exist");
        }
        
        JooqGeneticSequence sequence = new JooqGeneticSequence();

        sequence.loadFrom(file, parser);
        sequence.save();
        
        return addSequence(sequence);
    }
    
    public FolderContentGeneticSequence removeSequenceAt(int index) {
        FolderContentGeneticSequence sequence = sequences.removeElementAt(index);
        sequence.delete();
        return sequence;
    }
}