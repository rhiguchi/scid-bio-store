package jp.scid.bio.store.sequence;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;

import jp.scid.bio.store.FileLibrary.SequenceFileType;
import jp.scid.bio.store.FileLibrary.SequenceUnit;
import jp.scid.bio.store.base.RecordModel;


public interface GeneticSequence extends RecordModel {

    void reload() throws IOException, ParseException;
    
    File getFile();

    String name();

    int length();

    String accession();

    Integer version();

    String definition();

    String source();

    String organism();

    Date date();

    SequenceUnit sequenceUnit();

    String moleculeType();
    
    SequenceFileType sequenceFileType();
}
