package jp.scid.bio.store.folder;

import java.io.File;
import java.io.IOException;

import jp.scid.bio.store.GeneticSequenceParser;
import jp.scid.bio.store.base.AbstractRecordModel;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;

public class DefaultFolderContentGeneticSequence extends AbstractRecordModel<CollectionItemRecord>
        implements FolderContentGeneticSequence {
    private final GeneticSequence content;
    
    DefaultFolderContentGeneticSequence(GeneticSequence content, CollectionItemRecord record) {
        super(record);
        this.content = content;
    }

    public DefaultFolderContentGeneticSequence(GeneticSequence content) {
        this(content, null);
    }

    @Override
    public boolean save() {
        
        return super.save() && content.save();
    }
    
    @Override
    public void loadFrom(File file, GeneticSequenceParser parser) throws IOException {
        content.loadFrom(file, parser);
    }

    @Override
    public Long folderId() {
        return record.getFolderId();
    }

    @Override
    public Long sequenceId() {
        return content.id();
    }

    @Override
    protected CollectionItemRecord createRecord() {
        return new CollectionItemRecord();
    }
    
    @Override
    public File getFile() {
        // TODO Auto-generated method stub
        return null;
    }
}