package jp.scid.bio.store.sequence;

import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

public class FolderContentGeneticSequence extends GeneticSequence {
    private final CollectionItemRecord itemRecord;

    FolderContentGeneticSequence(GeneticSequenceRecord record,
            CollectionItemRecord itemRecord) {
        super(record);
        this.itemRecord = itemRecord;
    }
    
    @Override
    public Long id() {
        return itemRecord.getId();
    }
    
    public Long folderId() {
        return itemRecord.getFolderId();
    }
    
    public Long sequenceId() {
        return itemRecord.getGeneticSequenceId();
    }
}