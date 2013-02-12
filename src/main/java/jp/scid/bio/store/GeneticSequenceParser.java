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

import jp.scid.bio.sequence.SequenceBioDataFiles;
import jp.scid.bio.sequence.SequenceBioDataFormat;
import jp.scid.bio.sequence.SequenceBioDataReader;
import jp.scid.bio.sequence.fasta.Fasta;
import jp.scid.bio.sequence.fasta.FastaFormat;
import jp.scid.bio.sequence.genbank.GenBank;
import jp.scid.bio.sequence.genbank.GenBankFormat;
import jp.scid.bio.store.FileLibrary.SequenceFileType;
import jp.scid.bio.store.FileLibrary.SequenceUnit;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

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
        
        PushbackReader pbReader = new PushbackReader(reader, buf.length());
        pbReader.unread(buf.array(), 0, buf.length());
        BufferedReader source = new BufferedReader(pbReader);
        
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
            java.sql.Date date = data.locus().date() != null ?new java.sql.Date(data.locus().date().getTime()) : null;
            SequenceUnit unit = SequenceUnit.fromLabel(data.locus().sequenceUnit());
            
            GeneticSequenceRecord record = new GeneticSequenceRecord();
            record.setValue(GENETIC_SEQUENCE.NAME, data.name());
            record.setValue(GENETIC_SEQUENCE.LENGTH, data.sequenceLength());
            record.setValue(GENETIC_SEQUENCE.ACCESSION, data.accessionNumber());
            record.setValue(GENETIC_SEQUENCE.NAMESPACE, data.namespace());
            record.setValue(GENETIC_SEQUENCE.VERSION, data.accessionVersion());
            record.setValue(GENETIC_SEQUENCE.DEFINITION, data.description());
            record.setValue(GENETIC_SEQUENCE.SOURCE, data.source().value());
            record.setValue(GENETIC_SEQUENCE.ORGANISM, data.source().organism());
            record.setValue(GENETIC_SEQUENCE.DATE, date);
            record.setValue(GENETIC_SEQUENCE.UNIT, unit.index());
            record.setValue(GENETIC_SEQUENCE.MOLECULE_TYPE, data.locus().molculeType());
            record.setValue(GENETIC_SEQUENCE.FILE_TYPE, SequenceFileType.GENBANK.dbValue());
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
            record.setValue(GENETIC_SEQUENCE.NAME, data.name());
            record.setValue(GENETIC_SEQUENCE.LENGTH, data.sequenceLength());
            record.setValue(GENETIC_SEQUENCE.ACCESSION, data.accessionNumber());
            record.setValue(GENETIC_SEQUENCE.NAMESPACE, data.namespace());
            record.setValue(GENETIC_SEQUENCE.VERSION, data.accessionVersion());
            record.setValue(GENETIC_SEQUENCE.DEFINITION, data.description());
            record.setValue(GENETIC_SEQUENCE.FILE_TYPE, SequenceFileType.FASTA.dbValue());
            
            records.add(record);
        }
        
        return records;
    }
}
