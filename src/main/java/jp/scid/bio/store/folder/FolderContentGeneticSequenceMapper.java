package jp.scid.bio.store.folder;

import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequenceRecordMapper;

import org.jooq.Record;
import org.jooq.RecordMapper;

public class FolderContentGeneticSequenceMapper implements RecordMapper<Record, FolderContentGeneticSequence> {
    private final static FolderContentGeneticSequenceMapper singleton = new FolderContentGeneticSequenceMapper();
    
    private FolderContentGeneticSequenceMapper() {
    }
    
    public static FolderContentGeneticSequenceMapper basicMapper() {
        return singleton;
    }
    
    @Override
    public FolderContentGeneticSequence map(Record record) {
        GeneticSequenceRecord seqRecord = record.into(Tables.GENETIC_SEQUENCE);
        GeneticSequence seq = GeneticSequenceRecordMapper.basicMapper().map(seqRecord);
        
        CollectionItemRecord item = record.into(Tables.COLLECTION_ITEM);
        DefaultFolderContentGeneticSequence content = new DefaultFolderContentGeneticSequence(seq, item);
        return content;
    }
}
