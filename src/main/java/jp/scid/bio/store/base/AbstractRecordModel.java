package jp.scid.bio.store.base;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.UpdatableRecord;
import org.jooq.impl.Factory;

public abstract class AbstractRecordModel<R extends UpdatableRecord<R>> implements RecordModel {
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

    public void attach(Configuration configuration) {
        record.attach(configuration);
    }
    
    @Override
    public <T> T getValue(Field<T> field) throws IllegalArgumentException {
        return record.getValue(field);
    }

    @Override
    public <T> void setValue(Field<T> field, T value) throws IllegalArgumentException {
        T oldValue = record.getValue(field);
        record.setValue(field, value);
        firePropertyChange(field.getName(), oldValue, value);
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

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }
}
