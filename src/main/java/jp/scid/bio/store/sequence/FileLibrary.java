package jp.scid.bio.store.sequence;

import static java.lang.String.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import jp.scid.bio.sequence.SequenceBioData;
import jp.scid.bio.sequence.SequenceBioDataReader;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.apache.commons.io.FileUtils;
import org.jooq.impl.EnumConverter;

public class FileLibrary implements JooqGeneticSequence.Source {
    private File sequenceFilesRoot;
    private final GeneticSequenceParser parser;
    
    public FileLibrary() {
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
    
    public void setSequenceFilesRoot(File sequenceFilesRoot) {
        if (sequenceFilesRoot == null)
            throw new IllegalArgumentException("sequenceFilesRoot must not be null");
        this.sequenceFilesRoot = sequenceFilesRoot;
    }
    
    @Override
    public String readContentSequence(File file, SequenceFileType fileType) {
        StringBuilder sb = new StringBuilder();
        
        try {
            SequenceBioDataReader<?> dataReader = parser.createBioDataReader(file, fileType);
            
            SequenceBioData data;
            while ((data = dataReader.readNext()) != null) {
                sb.append(data.sequence());
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        
        return sb.toString();
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
        final File outFileParent = outFile.getParentFile();
        
        // preparing directories
        if (!outFileParent.exists()) {
            outFileParent.mkdirs();
        }
        if (!outFileParent.canWrite())
            throw new IOException(format("writing to dir %s is not allowed", outFileParent));
        
        int baseCount = 1;
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
    }
}
