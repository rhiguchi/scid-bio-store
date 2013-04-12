package jp.scid.bio.store.element;

import jp.scid.bio.store.base.RecordModel;
import jp.scid.bio.store.collection.SequenceCollection;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;

/**
 * シーケンスデータを格納したフォルダの構造定義
 * @author higuchi
 *
 */
public abstract class SequenceFolder extends RecordModel<FolderRecord> {
    
    SequenceFolder(FolderRecord record) {
        super(record);
    }
    
    public static SequenceFolder newFolderOf(CollectionType type) {
        if (type == null) throw new IllegalArgumentException("type must not be null");
        return type.createFolder();
    }
    
    /**
     * このフォルダにある配列情報を返します。
     * 
     * @return 配列情報
     */
    public abstract SequenceCollection getContentSequences();

    /**
     * このフォルダの id を返します。
     * 
     * @return id 値
     */
    public Long id() {
        return record.getId();
    }
    
    /**
     * 親フォルダの id を返します。
     * 
     * @return 親フォルダの id。ルートフォルダで親がないときは {@code null} 。
     */
    public Long parentId() {
        return record.getParentId();
    }
    
    public void setParentId(Long newParentId) {
        record.setParentId(newParentId);
    }
    
    @Override
    protected FolderRecord createRecord() {
        return new FolderRecord();
    }

    public boolean store() {
        return record.store() > 0;
    }
    
    public boolean delete() {
        return record.delete() > 0;
    }

    public void setName(String newName) {
        record.setName(newName);
    }
}

class BasicSequenceFolder extends SequenceFolder {
    public BasicSequenceFolder(FolderRecord record) {
        super(record);
        // TODO Auto-generated constructor stub
    }

    @Override
    public SequenceCollection getContentSequences() {
        // TODO Auto-generated method stub
        return null;
    }
}

class FilterSequenceCollection extends SequenceFolder {
    public FilterSequenceCollection(FolderRecord record) {
        super(record);
    }

    @Override
    public SequenceCollection getContentSequences() {
        // TODO Auto-generated method stub
        return null;
    }
}