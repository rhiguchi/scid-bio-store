package jp.scid.bio.store.sequence;

import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

public interface FolderContentGeneticSequence extends GeneticSequence {
    Long folderId();
    
    Long sequenceId();
}

class FolderContentGeneticSequenceImpl extends GeneticSequenceImpl implements FolderContentGeneticSequence {
    private final CollectionItemRecord itemRecord;

    FolderContentGeneticSequenceImpl(GeneticSequenceRecord record,
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