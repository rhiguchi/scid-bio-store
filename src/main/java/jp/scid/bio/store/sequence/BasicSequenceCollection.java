package jp.scid.bio.store.sequence;

import static org.jooq.impl.Factory.*;

import java.util.LinkedList;
import java.util.List;

import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.impl.Factory;

public class BasicSequenceCollection extends MutableSequenceCollection<FolderContentGeneticSequence> {
    final static Field<Long> LAST_MODIFICATION = fieldByName(Long.class, "last_modification");
    final static Field<String> TABLE_NAME = fieldByName(String.class, "table_name");
    
    private final Source source;
    private long folderId;
    
    BasicSequenceCollection(Source source, long folderId) {
        super(source);
        
        this.source = source;
        this.folderId = folderId;
    }
    
    
    
    @Override
    protected List<FolderContentGeneticSequence> retrieve() {
        return source.retrieveFolderContent(folderId);
    }
    
    @Override
    public void addSequence(GeneticSequence sequence) {
        if (sequence == null) throw new IllegalArgumentException("sequence must not be null");
        
        FolderContentGeneticSequence content = source.addFolderContent(folderId, sequence);
        add(content);
    }
    
    public static interface Source extends MutableSequenceCollection.Source {
        List<FolderContentGeneticSequence> retrieveFolderContent(long folderId);
        
        FolderContentGeneticSequence addFolderContent(long folderId, GeneticSequence sequence);
    }
    
    static class DefaultSource extends MutableSequenceCollection.JooqSource implements Source, RecordMapper<Record, FolderContentGeneticSequence> {
        private final Factory create;
        
        public DefaultSource(Factory factory) {
            super();
            this.create = factory;
        }

        @Override
        public List<FolderContentGeneticSequence> retrieveFolderContent(long folderId) {
            LinkedList<Field<?>> fields = new LinkedList<Field<?>>(Tables.GENETIC_SEQUENCE.getFields());
            fields.addAll(0, Tables.COLLECTION_ITEM.getFields());
            
            Result<Record> result = create.select(fields)
                    .from(Tables.COLLECTION_ITEM)
                    .join(Tables.GENETIC_SEQUENCE)
                    .on(Tables.COLLECTION_ITEM.GENETIC_SEQUENCE_ID.eq(Tables.GENETIC_SEQUENCE.ID))
                    .orderBy(Tables.COLLECTION_ITEM.ID)
                    .fetch();
            return result.map(this);
        }
        
        @Override
        public FolderContentGeneticSequence map(Record record) {
            GeneticSequenceRecord seq = record.into(Tables.GENETIC_SEQUENCE);
            CollectionItemRecord item = record.into(Tables.COLLECTION_ITEM);
            return new FolderContentGeneticSequence(seq, item);
        }
        
        @Override
        public FolderContentGeneticSequence addFolderContent(long folderId, GeneticSequence sequence) {
            if (sequence.id() == null) {
                throw new IllegalStateException("id of sequence must not be null");
            }
            
            GeneticSequenceRecord record = sequence.getRecord();
            
            CollectionItemRecord item = create.newRecord(Tables.COLLECTION_ITEM);
            item.setFolderId(folderId);
            item.setGeneticSequenceId(record);
            item.store();
            
            return new FolderContentGeneticSequence(record, item);
        }
    }
}