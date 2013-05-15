package jp.scid.bio.store.folder;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import jp.scid.bio.store.GeneticSequenceParser;
import jp.scid.bio.store.base.AbstractRecordListModel;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.DefaultGeneticSequence;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;
import jp.scid.bio.store.sequence.SequenceCollection;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;

public class JooqBasicFolder extends AbstractFolder {
    private final BasicFolderSequenceCollection contents;
    
    private GeneticSequenceParser parser = null;
    
    JooqBasicFolder(FolderRecord record) {
        super(record);
        contents = new BasicFolderSequenceCollection();
    }
    
    public JooqBasicFolder() {
        this(new FolderRecord());
    }
    
    @Override
    public SequenceCollection<FolderContentGeneticSequence> getContentSequences() {
        return contents;
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

    public boolean canImport(File file) {
        return parser != null;
    }
    
    public FolderContentGeneticSequence importSequence(File file) throws IOException {
        if (parser == null) {
            throw new IllegalStateException("cannot import file because parser doesn't exist");
        }
        
        DefaultGeneticSequence sequence = new DefaultGeneticSequence();

        sequence.loadFrom(file, parser);
        sequence.save();
        
        return addSequence(sequence);
    }
    
    private class BasicFolderSequenceCollection extends AbstractRecordListModel<FolderContentGeneticSequence>
            implements SequenceCollection<FolderContentGeneticSequence> {
        @Override
        protected List<FolderContentGeneticSequence> retrieve() {
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
            return result.map(FolderContentGeneticSequenceMapper.basicMapper());
        }
    }
}