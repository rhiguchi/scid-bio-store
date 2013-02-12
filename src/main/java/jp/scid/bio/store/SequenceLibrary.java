package jp.scid.bio.store;

import java.util.List;

import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.impl.Factory;

public class SequenceLibrary {
    private final Factory create;
    
    SequenceLibrary(Factory factory) {
        this.create = factory;
    }

    public boolean addNew(GeneticSequenceRecord record) {
        int result = create.executeInsert(record);
        return result > 0;
    }

    public boolean delete(GeneticSequenceRecord record) {
        return create.executeDelete(record) > 0;
    }

    public List<GeneticSequenceRecord> findAll() {
        return create.fetch(Tables.GENETIC_SEQUENCE);
    }
    
    public boolean store(GeneticSequenceRecord record) {
        return create.executeUpdate(record) > 0;
    }
}
