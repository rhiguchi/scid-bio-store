package jp.scid.bio.store.folder;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import jp.scid.bio.store.GeneticSequenceParser;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;
import jp.scid.bio.store.sequence.BasicSequenceCollection;
import jp.scid.bio.store.sequence.DefaultGeneticSequence;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;
import jp.scid.bio.store.sequence.SequenceCollection;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Result;

class JooqBasicSequenceFolder extends AbstractFolder
        implements BasicSequenceFolder, RecordMapper<Record, FolderContentGeneticSequence> {
    private final BasicSequenceCollection contents;
    
    private GeneticSequenceParser parser = null;
    
    JooqBasicSequenceFolder(FolderRecord record) {
        super(record);
        contents = new BasicSequenceCollection(this);
    }
    
    public JooqBasicSequenceFolder() {
        this(new FolderRecord());
    }
    
    @Override
    public SequenceCollection<FolderContentGeneticSequence> getContentSequences() {
        return contents;
    }

    public List<FolderContentGeneticSequence> retrieveFolderContent() {
        long folderId = id();
        LinkedList<Field<?>> fields = new LinkedList<Field<?>>(Tables.GENETIC_SEQUENCE.getFields());
        fields.addAll(0, Tables.COLLECTION_ITEM.getFields());
        
        Result<Record> result = create().select(fields)
                .from(Tables.COLLECTION_ITEM)
                .join(Tables.GENETIC_SEQUENCE)
                .on(Tables.COLLECTION_ITEM.GENETIC_SEQUENCE_ID.eq(Tables.GENETIC_SEQUENCE.ID))
                .where(Tables.COLLECTION_ITEM.FOLDER_ID.eq(folderId))
                .orderBy(Tables.COLLECTION_ITEM.ID)
                .fetch();
        return result.map(this);
    }

    public FolderContentGeneticSequence addSequence(GeneticSequence sequence) {
        if (sequence.id() == null) {
            throw new IllegalStateException("id of sequence must not be null");
        }
        long folderId = id();
        
        GeneticSequenceRecord record = sequence.getRecord();
        
        CollectionItemRecord item = create().newRecord(Tables.COLLECTION_ITEM);
        item.setFolderId(folderId);
        item.setGeneticSequenceId(record);
        item.store();
        
        return new FolderContentGeneticSequenceImpl(record, item);
    }

    public boolean canAdd(File file) {
        return parser != null;
    }
    
    @Override
    public FolderContentGeneticSequence addSequence(File file) throws IOException {
        DefaultGeneticSequence sequence = new DefaultGeneticSequence();
        loadSequence(file, sequence);
        addSequence(sequence);
        return null;
    }

    private void loadSequence(File file, DefaultGeneticSequence sequence) throws IOException {
        if (parser == null) {
            throw new IllegalStateException("cannot import file because parser doesn't exist");
        }
        
        sequence.loadFrom(file, parser);
        sequence.save();
    }
    
    @Override
    public FolderContentGeneticSequence map(Record record) {
        GeneticSequenceRecord seq = record.into(Tables.GENETIC_SEQUENCE);
        CollectionItemRecord item = record.into(Tables.COLLECTION_ITEM);
        return new FolderContentGeneticSequenceImpl(seq, item);
    }
    
    static class FolderContentGeneticSequenceImpl extends DefaultGeneticSequence implements FolderContentGeneticSequence {
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
}