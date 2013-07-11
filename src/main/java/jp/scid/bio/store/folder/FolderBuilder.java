package jp.scid.bio.store.folder;

import jp.scid.bio.store.jooq.tables.records.FolderRecord;

public class FolderBuilder {
    private final AbstractFolder.Source folderSource;
    private CollectionType collectionType;
    private FolderRecord record;
    
    public FolderBuilder(AbstractFolder.Source folderSource) {
        if (folderSource == null)
            throw new IllegalArgumentException("folderSource must not be null");
        
        this.folderSource = folderSource;
        collectionType = CollectionType.BASIC;
        record = new FolderRecord();
    }
    
    public void setCollectionType(CollectionType collectionType) {
        if (collectionType == null)
            throw new IllegalArgumentException("collectionType must not be null");
        
        this.collectionType = collectionType;
    }
    
    public void setRecord(FolderRecord record) {
        if (record == null) throw new IllegalArgumentException("record must not be null");
        
        this.record = record;
    }
    

    public Folder build() {
        return collectionType.createFolder(record, folderSource);
    }
}
