package jp.scid.bio.store;

import static jp.scid.bio.store.jooq.Tables.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.text.ParseException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.scid.bio.sequence.SequenceBioData;
import jp.scid.bio.sequence.SequenceBioDataFiles;
import jp.scid.bio.sequence.SequenceBioDataFormat;
import jp.scid.bio.sequence.SequenceBioDataReader;
import jp.scid.bio.sequence.fasta.Fasta;
import jp.scid.bio.sequence.fasta.FastaFormat;
import jp.scid.bio.sequence.genbank.GenBank;
import jp.scid.bio.sequence.genbank.GenBankFormat;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;
import jp.scid.bio.store.sequence.SequenceFileType;
import jp.scid.bio.store.sequence.SequenceUnit;

public class GeneticSequenceParser {
    private final SequenceBioDataFiles dataFiles;
    
    private GenBankFormat genBankFormat = new GenBankFormat();
    private FastaFormat fastaFormat = new FastaFormat();

    public GeneticSequenceParser() {
        dataFiles = SequenceBioDataFiles.newDefaultFormats();
    }
    
    public List<GeneticSequenceRecord> parse(File file) throws IOException, ParseException {
        FileReader reader = new FileReader(file);
        return parse(reader);
    }
    
    public List<GeneticSequenceRecord> parse(Reader reader) throws IOException, ParseException {
        CharBuffer buf = CharBuffer.allocate(512);
        reader.read(buf);
        String fileHeadText = buf.flip().toString();
        
        Class<? extends SequenceBioDataFormat<?>> formatClass = dataFiles.findFormat(fileHeadText);
        
        BufferedReader source = createBufferedReader(buf, reader);
        
        List<GeneticSequenceRecord> records;
        
        if (formatClass == GenBankFormat.class) {
            records = parseGenBank(source);
        }
        else if (formatClass == FastaFormat.class) {
            records = parseFasta(source);
        }
        else {
            return Collections.emptyList();
        }
        
        return records;
    }

    public void reloadHead(GeneticSequenceRecord record, File file) throws IOException, ParseException {
        FileReader reader = new FileReader(file);
        try {
            reloadHead(record, reader);
        }
        catch (UnknownSequenceFormatException origin) {
            UnknownSequenceFormatException e = new UnknownSequenceFormatException(file);
            e.initCause(origin);
            throw e;
        }
        finally {
            reader.close();
        }
    }
    
    public void reloadHead(GeneticSequenceRecord record, Reader reader)
            throws IOException, UnknownSequenceFormatException, ParseException {
        CharBuffer buf = CharBuffer.allocate(512);
        reader.read(buf);
        
        SequenceBioDataFormat<?> bioDataFormat = findFormat(buf.flip().toString());
        if (bioDataFormat == null) {
            throw new UnknownSequenceFormatException();
        }
        
        BufferedReader source = createBufferedReader(buf, reader);
        
        SequenceBioDataReader<?> bioDataReader = bioDataFormat.createDataReader(source);
        SequenceBioData bioData = bioDataReader.readNext();
        
        if (bioData instanceof GenBank) {
            setRecordValues(record, (GenBank) bioData);
        }
        else if (bioData instanceof Fasta) {
            setRecordValues(record, (Fasta) bioData);
        }
        else {
            throw new ParseException("cannot read data as " + bioDataFormat, 0);
        }
    }
    
    private SequenceBioDataFormat<?> findFormat(String dataText) {
        Class<? extends SequenceBioDataFormat<?>> formatClass = dataFiles.findFormat(dataText);
        if (formatClass == GenBankFormat.class) {
            return genBankFormat;
        }
        else if (formatClass == FastaFormat.class) {
            return fastaFormat;
        }
        return null;
    }
    
    public List<GeneticSequenceRecord> parseGenBank(File file) throws IOException, ParseException {
        BufferedReader source = new BufferedReader(new FileReader(file));
        
        return parseGenBank(source);
    }

    public List<GeneticSequenceRecord> parseGenBank(BufferedReader source)
            throws IOException, ParseException {
        SequenceBioDataReader<GenBank> dataReader = genBankFormat.createDataReader(source);
        
        List<GeneticSequenceRecord> records = new LinkedList<GeneticSequenceRecord>();
        GenBank data;
        while ((data = dataReader.readNext()) != null) {
            GeneticSequenceRecord record = new GeneticSequenceRecord();
            setRecordValues(record, data);
            records.add(record);
        }
        
        return records;
    }

    
    public List<GeneticSequenceRecord> parseFasta(File file) throws IOException, ParseException {
        BufferedReader source = new BufferedReader(new FileReader(file));
        return parseFasta(source);
    }

    public List<GeneticSequenceRecord> parseFasta(BufferedReader source)
            throws IOException, ParseException {
        SequenceBioDataReader<Fasta> dataReader = fastaFormat.createDataReader(source);
        
        List<GeneticSequenceRecord> records = new LinkedList<GeneticSequenceRecord>();
        Fasta data;
        while ((data = dataReader.readNext()) != null) {
            GeneticSequenceRecord record = new GeneticSequenceRecord();
            setRecordValues(record, data);
            
            records.add(record);
        }
        
        return records;
    }

    private void setRecordValues(GeneticSequenceRecord record, GenBank data) {
        java.sql.Date date = data.locus().date() != null ?new java.sql.Date(data.locus().date().getTime()) : null;
        SequenceUnit unit = SequenceUnit.fromLabel(data.locus().sequenceUnit());
        
        record.setValue(GENETIC_SEQUENCE.NAME, data.name());
        record.setValue(GENETIC_SEQUENCE.LENGTH, data.sequenceLength());
        record.setValue(GENETIC_SEQUENCE.ACCESSION, data.accessionNumber());
        record.setValue(GENETIC_SEQUENCE.NAMESPACE, data.namespace());
        record.setValue(GENETIC_SEQUENCE.VERSION, data.accessionVersion());
        record.setValue(GENETIC_SEQUENCE.DEFINITION, data.description());
        record.setValue(GENETIC_SEQUENCE.SOURCE, data.source().value());
        record.setValue(GENETIC_SEQUENCE.ORGANISM, data.source().organism());
        record.setValue(GENETIC_SEQUENCE.DATE, date);
        record.setValue(GENETIC_SEQUENCE.UNIT, unit.dbValue());
        record.setValue(GENETIC_SEQUENCE.MOLECULE_TYPE, data.locus().molculeType());
        record.setValue(GENETIC_SEQUENCE.FILE_TYPE, SequenceFileType.GENBANK.dbValue());
    }
    
    private void setRecordValues(GeneticSequenceRecord record, Fasta data) {
        record.setValue(GENETIC_SEQUENCE.NAME, data.name());
        record.setValue(GENETIC_SEQUENCE.LENGTH, data.sequenceLength());
        record.setValue(GENETIC_SEQUENCE.ACCESSION, data.accessionNumber());
        record.setValue(GENETIC_SEQUENCE.NAMESPACE, data.namespace());
        record.setValue(GENETIC_SEQUENCE.VERSION, data.accessionVersion());
        record.setValue(GENETIC_SEQUENCE.DEFINITION, data.description());
        
        record.setValue(GENETIC_SEQUENCE.SOURCE, "");
        record.setValue(GENETIC_SEQUENCE.ORGANISM, "");
        record.setValue(GENETIC_SEQUENCE.DATE, null);
        record.setValue(GENETIC_SEQUENCE.UNIT, SequenceUnit.UNKNOWN.dbValue());
        record.setValue(GENETIC_SEQUENCE.MOLECULE_TYPE, "");
        
        record.setValue(GENETIC_SEQUENCE.FILE_TYPE, SequenceFileType.FASTA.dbValue());
    }

    private static BufferedReader createBufferedReader(CharBuffer headBuffer, Reader reader)
            throws IOException {
        PushbackReader pbReader = new PushbackReader(reader, headBuffer.length());
        pbReader.unread(headBuffer.array(), 0, headBuffer.length());
        BufferedReader source = new BufferedReader(pbReader);
        return source;
    }

    public static class UnknownSequenceFormatException extends ParseException {
        private final File file;
        
        public UnknownSequenceFormatException() {
            this(null);
        }

        public UnknownSequenceFormatException(File file) {
            super("Unknown Format: " + file, 0);
            this.file = file;
        }
        
        public File getFile() {
            return file;
        }
    }
}
