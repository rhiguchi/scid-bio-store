package jp.scid.bio.store.base;

import org.jooq.Field;
import org.jooq.Record;

public interface RecordModel<R extends Record> {

    Long id();
    
    boolean delete();
    
    boolean save();
    
    boolean changed();
    
    R getRecord();
    
    <T> T getValue(Field<T> field) throws IllegalArgumentException;
    
    <T> void setValue(Field<T> field, T value);
}