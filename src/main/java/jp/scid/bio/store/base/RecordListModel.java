package jp.scid.bio.store.base;

import static org.jooq.impl.Factory.*;
import jp.scid.bio.store.sequence.GeneticSequence;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.jooq.impl.Factory;

public abstract class RecordListModel<E extends RecordModel<?>> extends PersistentListModel<E> {
    private final static Table<?> INFORMATION_SCHEMA_TABLES = tableByName("INFORMATION_SCHEMA", "TABLES");
    private final static Field<Long> LAST_MODIFICATION = fieldByName(Long.class, "LAST_MODIFICATION");
    private final static Field<String> TABLE_NAME = fieldByName(String.class, "TABLE_NAME");

    public RecordListModel() {
    }
    
    @Override
    protected Long getId(E element) {
        return element.id();
    }
    
    @Override
    protected boolean deleteFromStore(E element) {
        return element.delete();
    }
    
    @Override
    protected boolean update(E element) {
        return element.save();
    }
    
    @Override
    protected boolean insertIntoStore(E element) {
        element.setId(null);
        return element.save();
    }
    
//    @Override
//    protected long retrieveModificationValue() {
//        long value = create.select(LAST_MODIFICATION)
//                .from(INFORMATION_SCHEMA_TABLES)
//                .where(TABLE_NAME.eq(getTableName()))
//                .fetchOne(LAST_MODIFICATION);
//        return value;
//    }
}
