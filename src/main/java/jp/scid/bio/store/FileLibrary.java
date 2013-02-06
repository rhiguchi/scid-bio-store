package jp.scid.bio.store;

import static jp.scid.bio.store.jooq.Tables.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.Queue;

import jp.scid.bio.sequence.SequenceBioDataFiles;
import jp.scid.bio.sequence.SequenceBioDataFormat;
import jp.scid.bio.sequence.SequenceBioDataReader;
import jp.scid.bio.sequence.fasta.Fasta;
import jp.scid.bio.sequence.fasta.FastaFormat;
import jp.scid.bio.sequence.genbank.GenBank;
import jp.scid.bio.sequence.genbank.GenBankFormat;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.impl.Factory;

public class FileLibrary {
    private final SequenceBioDataFiles dataFiles;
    private final Factory create;
    
    private GenBankFormat genBankFormat = new GenBankFormat();
    private FastaFormat fastaFormat = new FastaFormat();
    
    public FileLibrary(Factory create) {
        this.create = create;
        
        dataFiles = SequenceBioDataFiles.newDefaultFormats();
    }
    
    public GeneticSequenceRecord add(File file) throws UnknownSequenceFormatException, IOException, ParseException {
        Class<? extends SequenceBioDataFormat<?>> foundFormatClass = dataFiles.findFormat(file);
        
        GeneticSequenceRecord entry;
        
        if (foundFormatClass == GenBankFormat.class) {
            entry = addGenBank(file);
        }
        else if (foundFormatClass == FastaFormat.class) {
            entry = addFasta(file);
        }
        else {
            throw new UnknownSequenceFormatException(file);
        }
        
        return entry;
    }
    
    public GeneticSequenceRecord addGenBank(File file) throws IOException, ParseException {
        BufferedReader source = new BufferedReader(new FileReader(file));
        SequenceBioDataReader<GenBank> dataReader = genBankFormat.createDataReader(source);
        
        Queue<GeneticSequenceRecord> records = new LinkedList<GeneticSequenceRecord>();
        GenBank data;
        while ((data = dataReader.readNext()) != null) {
            java.sql.Date date =
                    data.locus().date() != null ? new java.sql.Date(data.locus().date().getTime()) : null;
            
            GeneticSequenceRecord record = create.insertInto(Tables.GENETIC_SEQUENCE)
                    .set(GENETIC_SEQUENCE.NAME, data.name())
                    .set(GENETIC_SEQUENCE.LENGTH, data.sequenceLength())
                    .set(GENETIC_SEQUENCE.ACCESSION, data.accessionNumber())
                    .set(GENETIC_SEQUENCE.NAMESPACE, data.namespace())
                    .set(GENETIC_SEQUENCE.VERSION, data.accessionVersion())
                    .set(GENETIC_SEQUENCE.DEFINITION, data.description())
                    .set(GENETIC_SEQUENCE.SOURCE_TEXT, data.source().value())
                    .set(GENETIC_SEQUENCE.ORGANISM, data.source().organism())
                    .set(GENETIC_SEQUENCE.DATE, date)
                    .set(GENETIC_SEQUENCE.UNIT, data.locus().sequenceUnit())
                    .set(GENETIC_SEQUENCE.MOLECULE_TYPE, data.locus().molculeType())
                    .set(GENETIC_SEQUENCE.FILE_TYPE, 1)
                    .set(GENETIC_SEQUENCE.FILE_URI, file.toURI().toString())
                    .returning().fetchOne();
            records.add(record);
        }
        
        
        return records.peek();
    }
    
    public GeneticSequenceRecord addFasta(File file) throws IOException, ParseException {
        BufferedReader source = new BufferedReader(new FileReader(file));
        SequenceBioDataReader<Fasta> dataReader = fastaFormat.createDataReader(source);
        
        Queue<GeneticSequenceRecord> records = new LinkedList<GeneticSequenceRecord>();
        Fasta data;
        while ((data = dataReader.readNext()) != null) {
            GeneticSequenceRecord record = create.insertInto(Tables.GENETIC_SEQUENCE)
                    .set(GENETIC_SEQUENCE.NAME, data.name())
                    .set(GENETIC_SEQUENCE.LENGTH, data.sequenceLength())
                    .set(GENETIC_SEQUENCE.ACCESSION, data.accessionNumber())
                    .set(GENETIC_SEQUENCE.NAMESPACE, data.namespace())
                    .set(GENETIC_SEQUENCE.VERSION, data.accessionVersion())
                    .set(GENETIC_SEQUENCE.DEFINITION, data.description())
                    .set(GENETIC_SEQUENCE.FILE_TYPE, 2)
                    .set(GENETIC_SEQUENCE.FILE_URI, file.toURI().toString())
                    .returning().fetchOne();
            records.add(record);
        }
        
        return records.peek();
    }
    
    public static class UnknownSequenceFormatException extends IOException {
        private final File file;
        
        public UnknownSequenceFormatException() {
            this(null);
        }

        public UnknownSequenceFormatException(File file) {
            super("Unknown Format: " + file);
            this.file = file;
        }
        
        public File getFile() {
            return file;
        }
    }
}
