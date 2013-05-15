package jp.scid.bio.store.base;

import org.jooq.Field;

public interface RecordModel {

    Long id();
    
    boolean delete();
    
    boolean save();
    
    boolean changed();
    
    <T> T getValue(Field<T> field) throws IllegalArgumentException;
    
    <T> void setValue(Field<T> field, T value);
}