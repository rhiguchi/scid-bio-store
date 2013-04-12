package jp.scid.bio.store.collection;

import static org.jooq.impl.Factory.*;
import jp.scid.bio.store.base.PersistentListModel;

import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.Factory;

public abstract class JooqTableContents<E> extends PersistentListModel<E> {
    private final static Table<?> INFORMATION_SCHEMA_TABLES = tableByName("INFORMATION_SCHEMA", "TABLES");
    private final static Field<Long> LAST_MODIFICATION = fieldByName(Long.class, "LAST_MODIFICATION");
    private final static Field<String> TABLE_NAME = fieldByName(String.class, "TABLE_NAME");

    protected final Factory create;

    public JooqTableContents(Factory factory) {
        this.create = factory;
    }
    
    public abstract String getTableName();
    
    @Override
    protected long retrieveModificationValue() {
        long value = create.select(LAST_MODIFICATION)
                .from(INFORMATION_SCHEMA_TABLES)
                .where(TABLE_NAME.eq(getTableName()))
                .fetchOne(LAST_MODIFICATION);
        return value;
    }
}
