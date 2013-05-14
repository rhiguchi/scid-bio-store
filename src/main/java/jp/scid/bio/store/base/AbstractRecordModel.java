package jp.scid.bio.store.base;

import java.beans.PropertyChangeSupport;

import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.Factory;

public abstract class AbstractRecordModel<R extends Record> implements RecordModel<R> {
    protected final R record;
    private final PropertyChangeSupport pcs;

    public AbstractRecordModel() {
        this(null);
    }
    
    public AbstractRecordModel(R record) {
        this.record = record == null ? createRecord() : record;
        
        pcs = new PropertyChangeSupport(this);
    }
    
    Factory getFactory() {
        return (Factory) ((AttachableInternal) record).getConfiguration();
    }
    
    abstract protected R createRecord();

    abstract protected void setId(Long id);
    
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
    
    public R getRecord() {
        return record;
    }
}