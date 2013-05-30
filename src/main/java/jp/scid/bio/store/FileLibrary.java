package jp.scid.bio.store;

import static java.lang.String.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;
import jp.scid.bio.store.sequence.JooqGeneticSequence;

import org.apache.commons.io.FileUtils;
import org.jooq.impl.EnumConverter;

public class FileLibrary implements JooqGeneticSequence.Source {
    private File sequenceFilesRoot;
    private final GeneticSequenceParser parser;
    
    FileLibrary() {
        sequenceFilesRoot = new File(".", "GenomeMuseum Sequences");
        parser = new GeneticSequenceParser();
    }
    
    public static FileLibrary newFileLibrary(File filesRoot) {
        FileLibrary lib = new FileLibrary();
        lib.sequenceFilesRoot = filesRoot;
        return lib;
    }

    @Override
    public File getSequenceFilesRootDir() {
        return sequenceFilesRoot;
    }
    
    @Override
    public void loadSequence(GeneticSequenceRecord record, File file) throws IOException, ParseException {
        parser.reloadHead(record, file);
    }
    
    @Override
    public File saveFileToLibrary(GeneticSequenceRecord record, File sourceFile) throws IOException {
        SequenceFileType fileType = SequenceFileType.fromDbValue(record.getFileType());
        String extension = getExtension(fileType);
        String baseName =
            getBaseName(record.getName(), record.getAccession(), record.getDefinition());

        File outFile = new File(sequenceFilesRoot, baseName + extension);
        int baseCount = 1;
        
        File outFileDir = outFile.getParentFile();
        if (!outFileDir.exists()) {
            outFile.getParentFile().mkdirs();
        }
        
        if (!outFileDir.canWrite())
            throw new IOException(format("writing to dir %s is not allowed", outFile));
        
        while (!outFile.createNewFile()) {
            outFile = new File(sequenceFilesRoot, baseName + " " + baseCount++ + extension);
        }
        
        FileUtils.copyFile(sourceFile, outFile, true);
        
        return outFile;
    }
    

    String getBaseName(String name, String accession, String definition) {
        final String baseName;
        
        if (name != null && !name.isEmpty()) {
            baseName = name;
        }
        else if (accession != null && !accession.isEmpty()) {
            baseName = accession;
        }
        else if (definition != null && !definition.isEmpty()) {
            baseName = definition;
        }
        else {
            baseName = "Untitled";
        }
        return baseName;
    }

    private String getExtension(SequenceFileType fileType) {
        return fileType.defaultExtension();
    }
    
    
    public class SequenceFileTypeConverter extends EnumConverter<Short, SequenceFileType> {

        public SequenceFileTypeConverter() {
            super(Short.class, SequenceFileType.class);
        }
        
        @Override
        public Short to(SequenceFileType userObject) {
            // TODO Auto-generated method stub
            return super.to(userObject);
        }
    }
    
    public enum SequenceUnit {
        UNKNOWN((short) 0, ""),
        BASE_PAIR((short) 1, "bp"),
        AMINO_ACID((short) 2, "aa");
        
        private final short index;
        private final String label;

        private SequenceUnit(short index, String label) {
            this.index = index;
            this.label = label;
        }

        public short index() {
            return index;
        }
        
        public static SequenceUnit fromLabel(String label) {
            if (BASE_PAIR.label.equals(label)) {
                return BASE_PAIR;
            }
            else if (AMINO_ACID.label.equals(label)) {
                return AMINO_ACID;
            }
            return UNKNOWN;
        }

        public static SequenceUnit fromDbValue(short number) {
            return values()[number];
        }
    }
    
    public enum SequenceFileType {
        UNKNOWN(0),
        GENBANK(1, ".gbk"),
        FASTA(2, ".fasta"),
        ;
        
        private final int number;
        private final String defaultExtension;
        
        private SequenceFileType(int number, String defaultExtension) {
            this.number = number;
            this.defaultExtension = defaultExtension;
        }
        private SequenceFileType(int number) {
            this(number, ".txt");
        }
        
        public short dbValue() {
            return (short) number;
        }
        
        public String defaultExtension() {
            return defaultExtension;
        }
        
        public static SequenceFileType fromDbValue(short number) {
            return values()[number];
        }
    }
}
