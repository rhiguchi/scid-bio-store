package jp.scid.bio.store.collection;

import static org.jooq.impl.Factory.*;

import java.io.File;
import java.io.IOException;

import javax.swing.ListModel;

import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.Factory;

public interface SequenceCollection extends ListModel, Iterable<GeneticSequenceRecord> {
    public void fetch();
    
    public GeneticSequenceRecord getElementAt(int index);
}

interface MutableSequenceCollection extends SequenceCollection {
    /**
     * 
     * @param record
     * @return new index
     */
    public int addElement(Long key, GeneticSequenceRecord record);
    
    /**
     * 
     * @param file
     * @return new index
     * @throws IOException
     */
    public int addElementFromFile(File file) throws IOException;
    
    public void removeElement(int index);
    
    public void elementChanged(int index);
}

class LastModificationCheck {
    private final static Table<?> INFORMATION_SCHEMA_TABLES = tableByName("INFORMATION_SCHEMA", "TABLES");
    private final static Field<Long> LAST_MODIFICATION = fieldByName(Long.class, "LAST_MODIFICATION");
    private final static Field<String> TABLE_NAME = fieldByName(String.class, "TABLE_NAME");
    
    private final Factory factory;
    private final String tableName;
    
    private long lastValue = 0;
    
    public LastModificationCheck(Factory factory, String tableName) {
        this.factory = factory;
        this.tableName = tableName;
    }

    public boolean checkUpdated() {
        long value = factory.select(LAST_MODIFICATION)
                .from(INFORMATION_SCHEMA_TABLES)
                .where(TABLE_NAME.eq(tableName))
                .fetchOne(LAST_MODIFICATION);
        
        return lastValue < (lastValue = value);
    }
}

