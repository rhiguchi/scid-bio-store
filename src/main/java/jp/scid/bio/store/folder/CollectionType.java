package jp.scid.bio.store.folder;

import jp.scid.bio.store.jooq.tables.records.FolderRecord;

import org.jooq.Converter;
import org.jooq.impl.EnumConverter;

public enum CollectionType {
    NODE() {
        @Override
        public Folder createFolder(FolderRecord record) {
            record.setType(getDbValue());
            return new GroupFolderImpl(record);
        }
    },
    BASIC() {
        @Override
        public Folder createFolder(FolderRecord record) {
            return new BasicSequenceFolder(record);
        }
    },
    FILTER() {
        @Override
        public Folder createFolder(FolderRecord record) {
            return new FilterSequenceCollection(record);
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
    
    public Folder createSequenceCollection(FolderRecord record) {
        record.setType(getDbValue());
        Folder folder = createFolder(record);
        return folder;
    }
    
    public Folder createFolder() {
        return createFolder(new FolderRecord());
    }
    
    abstract Folder createFolder(FolderRecord record);
}