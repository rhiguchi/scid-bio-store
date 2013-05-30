package jp.scid.bio.store.folder;

import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequenceRecordMapper;
import jp.scid.bio.store.sequence.JooqGeneticSequence;

import org.jooq.Record;
import org.jooq.RecordMapper;

public class FolderContentGeneticSequenceMapper implements RecordMapper<Record, FolderContentGeneticSequence> {
    private final RecordMapper<GeneticSequenceRecord, GeneticSequence> mapper;
    
    public FolderContentGeneticSequenceMapper(RecordMapper<GeneticSequenceRecord, GeneticSequence> mapper) {
        this.mapper = mapper;
    }
    
    FolderContentGeneticSequenceMapper(JooqGeneticSequence.Source geneticSequenceSource) {
        this(new GeneticSequenceRecordMapper(geneticSequenceSource));
    }
    
    @Override
    public FolderContentGeneticSequence map(Record record) {
        GeneticSequenceRecord seqRecord = record.into(Tables.GENETIC_SEQUENCE);
        GeneticSequence seq = mapper.map(seqRecord);
        
        CollectionItemRecord item = record.into(Tables.COLLECTION_ITEM);
        DefaultFolderContentGeneticSequence content = new DefaultFolderContentGeneticSequence(seq, item);
        return content;
    }
}
