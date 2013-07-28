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
        URI uri = getFileUriAsUri();
        
        if (uri == null) {
            return null;
        }
        if (!uri.isAbsolute()) {
            return new File(source.getSequenceFilesRootDir(), uri.getPath());
        }
        return new File(uri);
    }

    private URI getFileUriAsUri() {
        String uriString = record.getFileUri();
        if (uriString == null) {
            return null;
        }
        return URI.create(uriString);
    }
    
    public void setFileUri(File file) {
        URI uri;
        if (file == null) {
            uri = null;
        }
        else {
            uri = file.toURI();
        }
        setFileUri(uri);
    }
    
    private void setFileUri(URI uri) {
        if (uri == null) {
            record.setFileUri(null);
        }
        else {
            record.setFileUri(uri.toString());
        }
    }

    @Override
    public boolean saveFileToLibrary() throws IOException {
        String libDir = source.getSequenceFilesRootDir().getCanonicalPath();
        
        File file = getFile();
        if (file == null || file.getCanonicalPath().startsWith(libDir)) {
            return false;
        }
        
        File newFile = source.saveFileToLibrary(record, file);
        URI uri = source.getSequenceFilesRootDir().toURI().relativize(newFile.toURI());
        setFileUri(uri);
        
        return save();
    }
    
    public boolean isFileStoredInLibray() {
        URI uri = getFileUriAsUri();
        return uri != null && !uri.isAbsolute();
    }
    
    public void reload() throws IOException, ParseException {
        File file = getFile();
        if (file == null) {
            throw new IOException("source file is null");
        }
        
        source.loadSequence(record, file);
    }
    
    @Override
    public boolean deleteFileFromLibrary() {
        if (!isFileStoredInLibray()) {
            return false;
        }
        return getFile().delete();
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
    
    @Override
    public String sequence() {
        File file = getFile();
        if (file == null) {
            return "";
        }
        SequenceFileType fileType = sequenceFileType();
        if (fileType == SequenceFileType.UNKNOWN) {
            return "";
        }
        
        return source.readContentSequence(file, fileType);
    }
    
    public static interface Source {
        File getSequenceFilesRootDir();

        String readContentSequence(File file, SequenceFileType fileType);
        
        File saveFileToLibrary(GeneticSequenceRecord record, File file) throws IOException;

        void loadSequence(GeneticSequenceRecord record, File file) throws IOException, ParseException;
    }
}