package jp.scid.bio.store.element;

import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.Factory;

abstract class JooqRecordModel<R extends Record> {
    protected final R record;

    public JooqRecordModel() {
        this(null);
    }
    
    public JooqRecordModel(R record) {
        this.record = record == null ? createRecord() : record;
    }
    
    Factory getFactory() {
        return (Factory) ((AttachableInternal) record).getConfiguration();
    }
    
    abstract protected R createRecord();

    public void attach(Configuration configuration) {
        record.attach(configuration);
    }
    
    public <T> T getValue(Field<T> field) throws IllegalArgumentException {
        return record.getValue(field);
    }

    public <T> void setValue(Field<T> field, T value) {
        record.setValue(field, value);
    }

    public boolean changed() {
        return record.changed();
    }
}