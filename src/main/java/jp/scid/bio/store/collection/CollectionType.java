package jp.scid.bio.store.collection;

import org.jooq.Converter;
import org.jooq.impl.EnumConverter;
import org.jooq.impl.Factory;

public enum CollectionType {
    NODE() {
        @Override
        public AbstractFolderSequenceCollection createSequenceCollection(Factory factory, long folderId) {
            return new NodeSequenceCollection(factory, folderId);
        }
    },
    BASIC() {
        @Override
        public AbstractFolderSequenceCollection createSequenceCollection(Factory factory, long folderId) {
            return new BasicSequenceCollection(factory, folderId);
        }
    },
    FILTER() {
        @Override
        public AbstractFolderSequenceCollection createSequenceCollection(Factory factory, long folderId) {
            return new FilterSequenceCollection(factory, folderId);
        }
    };
    
    private static final EnumConverter<Short, CollectionType> converter =
            new EnumConverter<Short, CollectionType>(Short.class, CollectionType.class);
    
    private CollectionType() {
    }
    
    public static CollectionType fromRecordValue(int value) {
        return converter.from((short) value);
    }
    
    public static Converter<Short, CollectionType> getConverter() {
        return converter;
    }
    
    public short getDbValue() {
        return (short) this.ordinal();
    }
    
    abstract public AbstractFolderSequenceCollection createSequenceCollection(Factory factory, long folderId);
}