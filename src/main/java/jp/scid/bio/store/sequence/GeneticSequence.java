package jp.scid.bio.store.sequence;

import java.io.File;
import java.io.IOException;

import jp.scid.bio.store.GeneticSequenceParser;
import jp.scid.bio.store.base.RecordModel;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

public interface GeneticSequence extends RecordModel<GeneticSequenceRecord> {

    void loadFrom(File file, GeneticSequenceParser parser) throws IOException;
}
