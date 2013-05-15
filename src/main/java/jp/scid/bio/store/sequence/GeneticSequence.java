package jp.scid.bio.store.sequence;

import java.io.File;
import java.io.IOException;

import jp.scid.bio.store.GeneticSequenceParser;
import jp.scid.bio.store.base.RecordModel;


public interface GeneticSequence extends RecordModel {

    void loadFrom(File file, GeneticSequenceParser parser) throws IOException;
}
