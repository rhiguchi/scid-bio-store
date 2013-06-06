package jp.scid.bio.store.sequence;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.Date;
import java.text.ParseException;

import jp.scid.bio.store.base.AbstractRecordModel;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

public class JooqGeneticSequence extends AbstractRecordModel<GeneticSequenceRecord> implements GeneticSequence {
    private final Source source;
    
    public JooqGeneticSequence(GeneticSequenceRecord record, Source source) {
        super(record);
        if (source == null) throw new IllegalArgumentException("source must not be null");
        this.source = source;
    }
    
    @Override
    protected GeneticSequenceRecord createRecord() {
        return new GeneticSequenceRecord();
    }

    @Override
    public File getFile() {
        String uriString = record.getFileUri();
        if (uriString == null) {
            return null;
        }
        URI uri = URI.create(uriString);
        File file = new File(uri);
        
        if (!file.isAbsolute()) {
            file = new File(source.getSequenceFilesRootDir(), file.getPath());
        }
        return file;
    }
    
    public void setFileUri(File file) {
        if (file == null) {
            record.setFileUri(null);
            return;
        }
        
        record.setFileUri(file.toURI().toString());
    }

    @Override
    public boolean saveFileToLibrary() throws IOException {
        String libDir = source.getSequenceFilesRootDir().getCanonicalPath();
        
        File file = getFile();
        if (file == null || file.getCanonicalPath().startsWith(libDir)) {
            return false;
        }
        
        File newFile = source.saveFileToLibrary(record, file);
        setFileUri(newFile);
        
        return save();
    }
    
    public void reload() throws IOException, ParseException {
        File file = getFile();
        if (file == null) {
            throw new IOException("source file is null");
        }
        
        source.loadSequence(record, file);
    }
    
    @Override
    public Long id() {
        return record.getId();
    }
    
    public String name() {
        return record.getName();
    }

    public int length() {
        return record.getLength();
    }

    public String accession() {
        return record.getAccession();
    }

    public Integer version() {
        return record.getVersion();
    }

    public String definition() {
        return record.getDefinition();
    }

    public String source() {
        return record.getSource();
    }

    public String organism() {
        return record.getOrganism();
    }

    public Date date() {
        return record.getDate();
    }

    public SequenceUnit sequenceUnit() {
        return SequenceUnit.fromDbValue(record.getUnit());
    }

    public String moleculeType() {
        return record.getMoleculeType();
    }
    
    public SequenceFileType sequenceFileType() {
        return SequenceFileType.fromDbValue(record.getFileType());
    }
    
    public String namespace() {
        return record.getNamespace();
    }
    
    public static interface Source {
        File getSequenceFilesRootDir();

        File saveFileToLibrary(GeneticSequenceRecord record, File file) throws IOException;

        void loadSequence(GeneticSequenceRecord record, File file) throws IOException, ParseException;
    }
}