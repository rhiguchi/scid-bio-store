package jp.scid.bio.store.folder;

import jp.scid.bio.store.folder.FolderRecordGroupFolder.Source;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;

import org.jooq.Converter;
import org.jooq.impl.EnumConverter;

public enum CollectionType {
    NODE() {
        @Override
        public AbstractFolder createFolder(FolderRecord record, Source folderSource) {
            return new FolderRecordGroupFolder(record, folderSource);
        }
    },
    BASIC() {
        @Override
        public AbstractFolder createFolder(FolderRecord record, Source folderSource) {
            return new FolderRecordBasicFolder(record, folderSource);
        }
    },
    FILTER() {
        @Override
        public AbstractFolder createFolder(FolderRecord record, Source folderSource) {
            return new FolderRecordFilterFolder(record, folderSource);
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
    
    public Folder createSequenceCollection(FolderRecord record, Source folderSource) {
        record.setType(getDbValue());
        Folder folder = createFolder(record, folderSource);
        return folder;
    }
    
    abstract AbstractFolder createFolder(FolderRecord record, Source folderSource);
}