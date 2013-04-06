package jp.scid.bio.store;

import static org.jooq.impl.Factory.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import jp.scid.bio.store.AbstractSequenceCollection.IdentifiableRecord;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.h2.tools.TriggerAdapter;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.jooq.impl.Factory;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;
import org.jooq.util.GenerationTool;

public interface SequenceCollection extends ListModel, Iterable<GeneticSequenceRecord> {
    public void fetch();
    
    public GeneticSequenceRecord getElementAt(int index);
}

abstract class AbstractSequenceCollection extends AbstractListModel implements SequenceCollection {
    protected final Factory create;
    
    private final LastModificationCheck modificationCheck;
    
    private final List<GeneticSequenceRecord> elements;

    private final NavigableSet<Long> identifierSet;
    
    public AbstractSequenceCollection(Factory factory) {
        this.create = factory;
        modificationCheck = new LastModificationCheck(factory, Tables.GENETIC_SEQUENCE.getName());
        elements = new ArrayList<GeneticSequenceRecord>();
        identifierSet = new TreeSet<Long>();
    }
    
    public void fetch() {
        modificationCheck.checkUpdated();
        
        SortedMap<Long, GeneticSequenceRecord> recordMap = new TreeMap<Long, GeneticSequenceRecord>();
        
        for (IdentifiableRecord record: retrieve()) {
            recordMap.put(record.getKey(), record.getValue());
        }
        
        // deletion
        List<Integer> removedIndices = retainAll(recordMap.keySet());
        
        for (int index: removedIndices) {
            removeElement(index);
        }
        
        List<Integer> insertedIndices = addAll(recordMap.keySet());
        ArrayList<GeneticSequenceRecord> newValues = new ArrayList<GeneticSequenceRecord>(recordMap.values());
        
        int lastStart = 0;
        for (int insertedIndex: insertedIndices) {
            List<GeneticSequenceRecord> changes = newValues.subList(lastStart, insertedIndex);
            replaceElements(lastStart, changes);
            lastStart = insertedIndex;
            
            addElement(insertedIndex, newValues.get(insertedIndex));
        }
    }


    private List<Integer> retainAll(Set<Long> newRecords) {
        List<Integer> removedIndices = new LinkedList<Integer>();
        int index = 0;
        for (Iterator<Long> ite = identifierSet.iterator(); ite.hasNext(); index++) {
            Long key = ite.next();
            if (newRecords.contains(key)) {
                continue;
            }
            
            ite.remove();
            removedIndices.add(index);
        }
        
        return removedIndices;
    }
    
    private List<Integer> addAll(Collection<Long> newRecords) {
        List<Integer> insertedIndices = new LinkedList<Integer>();
        int index = 0;
        for (Iterator<Long> ite = newRecords.iterator(); ite.hasNext(); index++) {
            Long id = ite.next();
            
            if (!identifierSet.add(id)) {
                insertedIndices.add(index);
            }
        }
        
        return insertedIndices;
    }
    
    public boolean addOrUpdate(Long key, GeneticSequenceRecord record) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        if (record == null) throw new IllegalArgumentException("record must not be null");

        boolean isInserted = identifierSet.add(key);
        int index = identifierSet.headSet(key).size();
        if (isInserted) {
            addElement(index, record);
        }
        else {
            updateElement(index, record);
        }
        
        return isInserted;
    }

    private void updateElement(int index, GeneticSequenceRecord element) {
        elements.set(index, element);
        fireContentsChanged(this, index, index);
    }

    private void replaceElements(int startIndex, List<GeneticSequenceRecord> changes) {
        if (!changes.isEmpty()) {
            int end = startIndex + changes.size();
            elements.subList(startIndex, end).clear();
            elements.addAll(startIndex, changes);
            fireContentsChanged(this, startIndex, end - 1);
        }
    }
    
    private void addElement(int index, GeneticSequenceRecord element) {
        elements.add(index, element);
        fireIntervalAdded(this, index, index);
    }
    
    private void removeElement(int index) {
        elements.remove(index);
        fireIntervalRemoved(this, index, index);
    }
    
    protected abstract List<IdentifiableRecord> retrieve();
    
    @Override
    public GeneticSequenceRecord getElementAt(int index) {
        return elements.get(index);
    }
    
    @Override
    public int getSize() {
        return elements.size();
    }
    
    @Override
    public Iterator<GeneticSequenceRecord> iterator() {
        return elements.iterator();
    }
    
    static class IdentifiableRecord implements Entry<Long, GeneticSequenceRecord> {
        private final Long key;
        private GeneticSequenceRecord value;
        
        public IdentifiableRecord(Long key, GeneticSequenceRecord value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Long getKey() {
            return key;
        }

        @Override
        public GeneticSequenceRecord getValue() {
            return value;
        }

        @Override
        public GeneticSequenceRecord setValue(GeneticSequenceRecord value) {
            GeneticSequenceRecord old = this.value;
            this.value = value;
            return old;
        }
    }
    
    static class IdentifiableRecordMapper implements RecordMapper<Record, IdentifiableRecord> {
        private final Field<Long> idField;
        
        public IdentifiableRecordMapper(Field<Long> idField) {
            this.idField = idField;
        }

        @Override
        public IdentifiableRecord map(Record record) {
            Long id = record.getValue(idField);
            GeneticSequenceRecord gsRecord = record.into(Tables.GENETIC_SEQUENCE);
            return new IdentifiableRecord(id, gsRecord);
        }
    }
}

class BasicSequenceCollection extends AbstractSequenceCollection {
    final static Field<Long> LAST_MODIFICATION = fieldByName(Long.class, "last_modification");
    final static Field<String> TABLE_NAME = fieldByName(String.class, "table_name");
    
    private Long folderId = null;
    
    public BasicSequenceCollection(Factory factory) {
        super(factory);
    }
//
//    public void fetch() {
//        
//        
//        List<GeneticSequenceRecord> newElements = create.selectFrom(Tables.GENETIC_SEQUENCE)
//                .orderBy(Tables.GENETIC_SEQUENCE.ID.asc())
//                .fetch();
//        
//        elements.clear();
//        elements.addAll(newElements);
//    }
    
    @Override
    protected List<IdentifiableRecord> retrieve() {
        Field<Long> collectionItemIdField = Tables.COLLECTION_ITEM.ID.as("collection_item_id");
        LinkedList<Field<?>> fields = new LinkedList<Field<?>>(Tables.GENETIC_SEQUENCE.getFields());
        fields.addFirst(collectionItemIdField);
        
        Result<Record> result = create.select(fields)
                .from(Tables.COLLECTION_ITEM)
                .join(Tables.GENETIC_SEQUENCE)
                .on(Tables.COLLECTION_ITEM.GENETIC_SEQUENCE_ID.eq(Tables.GENETIC_SEQUENCE.ID))
                .orderBy(Tables.COLLECTION_ITEM.ID)
                .fetch();
        
        List<IdentifiableRecord> list = result.map(new IdentifiableRecordMapper(collectionItemIdField));
        return list;
    }
    
    public GeneticSequenceRecord addFile(File file) throws IOException {
        GeneticSequenceRecord newRecord = create.newRecord(Tables.GENETIC_SEQUENCE);
        newRecord.setId(null);
        newRecord.setName("Untitiled");
        
        addRecord(newRecord);
        
        return newRecord;
    }
    
    public void addRecord(GeneticSequenceRecord record) {
        if (record == null) throw new IllegalArgumentException("record must not be null");
        
        record.store();
        
        Long lookupId;
        
        if (folderId != null) {
            CollectionItemRecord item = create.newRecord(Tables.COLLECTION_ITEM);
            item.setFolderId(folderId);
            item.setGeneticSequenceId(record);
            item.store();
            
            lookupId = item.getId();
        }
        else {
            lookupId = record.getId();
        }
        
        addOrUpdate(lookupId, record);
    }
    
    private long retrieveTableLastModification() {
        long value = create.select(LAST_MODIFICATION)
                .from(tableByName("INFORMATION_SCHEMA", "TABLES"))
                .where(TABLE_NAME.eq("GENETIC_SEQUENCE"))
                .fetchOne(LAST_MODIFICATION);
        return value;
    }
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

