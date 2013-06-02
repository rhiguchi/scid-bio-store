package jp.scid.bio.store.folder;

import static jp.scid.bio.store.jooq.Tables.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import jp.scid.bio.store.folder.FolderRecordGroupFolder.Source;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;
import jp.scid.bio.store.sequence.JooqGeneticSequence;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.impl.Factory;

public class JooqFolderSource implements Source {
    private final Factory create;
    private final JooqGeneticSequence.Source geneticSequenceSource;
    
    public JooqFolderSource(JooqGeneticSequence.Source geneticSequenceSource, Factory factory) {
        super();
        this.create = factory;
        this.geneticSequenceSource = geneticSequenceSource;
    }

    @Override
    public Folder createFolder(CollectionType type, Long parentFolderId) {
        if (type == null) throw new IllegalArgumentException("type must not be null");
        
        FolderRecord folderRecord = create.newRecord(Tables.FOLDER);
        folderRecord.setName(getNewFolderName(type));
        folderRecord.setType(type.getDbValue());
        
        Folder folder = type.createFolder(folderRecord, this);
        folder.save();
        return folder;
    }
    
    protected String getNewFolderName(CollectionType type) {
        return "New " + type.name(); // TODO
    }

    @Override
    public List<Folder> retrieveFolderChildren(FoldersContainer parent, long parentFolderId) {
        RootFolderMapper mapper = new RootFolderMapper(this, parent);
        
        Result<FolderRecord> result = create.selectFrom(Tables.FOLDER)
                .where(FOLDER.PARENT_ID.eq(parentFolderId))
                .fetch();
        return result.map(mapper);
    }
    
    public List<Folder> retrieveRootFolders(FoldersContainer parent) {
        RootFolderMapper mapper = new RootFolderMapper(this, parent);
        
        Result<FolderRecord> result = create.selectFrom(Tables.FOLDER)
                .where(FOLDER.PARENT_ID.isNull())
                .fetch();
        return result.map(mapper);
    }
    
    @Override
    public GeneticSequence createGeneticSequence(File file)
            throws IOException, ParseException {
        GeneticSequenceRecord sequenceRecord = create.newRecord(Tables.GENETIC_SEQUENCE);
        sequenceRecord.setName("Untitiled");
        
        JooqGeneticSequence sequence = new JooqGeneticSequence(sequenceRecord, geneticSequenceSource);
        sequence.setFileUri(file);
        sequence.reload();
        
        return sequence;
    }
    
    @Override
    public FolderContentGeneticSequence createFolderContent(GeneticSequence sequence, Folder folder) {
        if (sequence.id() == null) {
            throw new IllegalStateException("id of sequence must not be null");
        }
        
        CollectionItemRecord item = create.newRecord(Tables.COLLECTION_ITEM);
        item.setFolderId(folder.id());
        item.setGeneticSequenceId(sequence.id());
        
        DefaultFolderContentGeneticSequence content =
                new DefaultFolderContentGeneticSequence(sequence, item);
        content.save();
        return content;
    }
    
    public FolderContentGeneticSequence addSequence(GeneticSequence sequence, Folder folder) {
        if (sequence.id() == null) {
            throw new IllegalStateException("id of sequence must not be null");
        }
        long folderId = folder.id();
        
        CollectionItemRecord item = create.newRecord(Tables.COLLECTION_ITEM);
        item.setFolderId(folderId);
        item.setGeneticSequenceId(sequence.id());
        
        DefaultFolderContentGeneticSequence content =
                new DefaultFolderContentGeneticSequence(sequence, item);
        
        content.save();
        
        return content;
    }
    
    @Override
    public List<FolderContentGeneticSequence> retrieveFolderContents(long folderId) {
        List<Long> folderIds = retrieveDescendantFolderIds(folderId);
        return retrieveAllFolderContents(folderIds);
    }
    
    public List<FolderContentGeneticSequence> retrieveAllFolderContents(Collection<Long> folderIds) {
        LinkedList<Field<?>> fields = new LinkedList<Field<?>>(Tables.GENETIC_SEQUENCE.getFields());
        fields.addAll(0, Tables.COLLECTION_ITEM.getFields());
        
        Result<Record> result = create.select(fields)
                .from(Tables.COLLECTION_ITEM)
                .join(Tables.GENETIC_SEQUENCE)
                .on(Tables.COLLECTION_ITEM.GENETIC_SEQUENCE_ID.eq(Tables.GENETIC_SEQUENCE.ID))
                .where(Tables.COLLECTION_ITEM.FOLDER_ID.in(folderIds))
                .orderBy(Tables.COLLECTION_ITEM.ID)
                .fetch();
        
        FolderContentGeneticSequenceMapper mapper = new FolderContentGeneticSequenceMapper(geneticSequenceSource);
        return result.map(mapper);
    }
    
    private List<Long> retrieveDescendantFolderIds(long start) {
        List<Long> leafFolderIds = new LinkedList<Long>();
        
        Queue<Long> remainingIdQueue = new ArrayDeque<Long>();
        remainingIdQueue.add(start);
        
        while (!remainingIdQueue.isEmpty()) {
            long folderId = remainingIdQueue.remove();
            
            CollectionType collectionType = getFolderType(folderId);
            if (collectionType != CollectionType.NODE) {
                leafFolderIds.add(folderId);
                continue;
            }
            
            List<Long> children = retrieveChildFolderIds(folderId);
            remainingIdQueue.addAll(children);
        }
        
        return leafFolderIds;
    }
    
    private CollectionType getFolderType(long folderId) {
        Short folderTypeValue = create.select(FOLDER.TYPE).from(Tables.FOLDER)
                .where(FOLDER.ID.eq(folderId))
                .fetchOne(FOLDER.TYPE);
        return CollectionType.fromRecordValue(folderTypeValue);
    }
    
    private List<Long> retrieveChildFolderIds(long folderId) {
        List<Long> result = create.select(FOLDER.ID)
                .from(Tables.FOLDER)
                .where(FOLDER.PARENT_ID.eq(folderId))
                .fetch(FOLDER.ID);
        return result;
    }
    
    static class RootFolderMapper implements RecordMapper<FolderRecord, Folder> {
        private final FolderBuilder builder;
        
        public RootFolderMapper(Source source, FoldersContainer parent) {
            if (source == null) throw new IllegalArgumentException("source must not be null");
            if (parent == null) throw new IllegalArgumentException("parent must not be null");
            
            builder = new FolderBuilder(source);
            builder.setParent(parent);
        }
        
        @Override
        public final Folder map(FolderRecord record) {
            builder.setRecord(record);
            
            CollectionType collectionType = CollectionType.fromRecordValue(record.getType());
            builder.setCollectionType(collectionType);
            
            return builder.build();
        }
    }
}