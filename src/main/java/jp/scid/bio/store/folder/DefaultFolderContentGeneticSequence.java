package jp.scid.bio.store.folder;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;

import jp.scid.bio.store.base.AbstractRecordModel;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;
import jp.scid.bio.store.sequence.SequenceFileType;
import jp.scid.bio.store.sequence.SequenceUnit;

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
    public Long id() {
        return record.getId();
    }
    
    @Override
    public boolean save() {
        return super.save() && content.save();
    }
    
    @Override
    public void reload() throws IOException, ParseException {
        content.reload();
    }
    
    @Override
    public boolean saveFileToLibrary() throws IOException {
        return content.saveFileToLibrary();
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
        return content.getFile();
    }

    @Override
    public String name() {
        return content.name();
    }

    @Override
    public int length() {
        return content.length();
    }

    @Override
    public String accession() {
        return content.accession();
    }

    @Override
    public Integer version() {
        return content.version();
    }

    @Override
    public String definition() {
        return content.definition();
    }

    @Override
    public String source() {
        return content.source();
    }

    @Override
    public String organism() {
        return content.organism();
    }

    @Override
    public Date date() {
        return content.date();
    }

    @Override
    public String namespace() {
        return content.namespace();
    }
    
    @Override
    public SequenceUnit sequenceUnit() {
        return content.sequenceUnit();
    }

    @Override
    public String moleculeType() {
        return content.moleculeType();
    }

    @Override
    public SequenceFileType sequenceFileType() {
        return content.sequenceFileType();
    }
    
    @Override
    public String sequence() {
        return content.sequence();
    }
}