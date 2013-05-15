package jp.scid.bio.store.base;

import static org.jooq.impl.Factory.*;

import javax.swing.ListModel;

import org.jooq.Field;
import org.jooq.Table;

public abstract class AbstractRecordListModel<E extends RecordModel>
        extends PersistentListModel<E> implements ListModel {
    private final static Table<?> INFORMATION_SCHEMA_TABLES = tableByName("INFORMATION_SCHEMA", "TABLES");
    private final static Field<Long> LAST_MODIFICATION = fieldByName(Long.class, "LAST_MODIFICATION");
    private final static Field<String> TABLE_NAME = fieldByName(String.class, "TABLE_NAME");

    public AbstractRecordListModel() {
    }
    
    @Override
    protected Long getId(E element) {
        return element.id();
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