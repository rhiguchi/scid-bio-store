package jp.scid.bio.store.base;

import java.beans.PropertyChangeSupport;

import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.UpdatableRecord;
import org.jooq.impl.Factory;

public abstract class AbstractRecordModel<R extends UpdatableRecord<R>> implements RecordModel {
    protected final R record;
    @SuppressWarnings("unused")
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

    public void attach(Configuration configuration) {
        record.attach(configuration);
    }
    
    @Override
    public <T> T getValue(Field<T> field) throws IllegalArgumentException {
        return record.getValue(field);
    }

    @Override
    public <T> void setValue(Field<T> field, T value) {
        record.setValue(field, value);
    }
    
    @Override
    public Long id() {
        return record.getValue("id", Long.class);
    }
    
    @Override
    public boolean changed() {
        return record.changed();
    }
    
    @Override
    public boolean delete() {
        return record.delete() > 0;
    }
    
    @Override
    public boolean save() {
        return record.store() > 0;
    }
    
    public R getRecord() {
        return record;
    }
}
