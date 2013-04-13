package jp.scid.bio.store.folder;

import jp.scid.bio.store.base.AbstractRecordModel;
import jp.scid.bio.store.base.RecordModel;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.BasicSequenceCollection;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.SequenceCollection;

/**
 * シーケンスデータを格納したフォルダの構造定義
 * @author higuchi
 *
 */
public interface Folder extends RecordModel<FolderRecord> {
    Long parentId();
    
    void setParentId(Long newParentId);
    
    void setName(String newName);
}

abstract class AbstractFolder extends AbstractRecordModel<FolderRecord> implements Folder {
    
    AbstractFolder(FolderRecord record) {
        super(record);
    }
    
    public static Folder newFolderOf(CollectionType type) {
        if (type == null) throw new IllegalArgumentException("type must not be null");
        return type.createFolder();
    }
    
    /**
     * このフォルダにある配列情報を返します。
     * 
     * @return 配列情報
     */
    public abstract SequenceCollection<FolderContentGeneticSequence> getContentSequences();

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

    @Override
    public boolean save() {
        return record.store() > 0;
    }
    
    @Override
    public boolean delete() {
        return record.delete() > 0;
    }

    @Override
    protected void setId(Long newId) {
        record.setId(newId);
    }
    
    public void setName(String newName) {
        record.setName(newName);
    }
}

class BasicSequenceFolder extends AbstractFolder {
    private final SequenceCollection<FolderContentGeneticSequence> contents;
    
    public BasicSequenceFolder(FolderRecord record) {
        super(record);
        contents = BasicSequenceCollection.fromFactory(null, record.getId());
    }
    
    @Override
    public SequenceCollection<FolderContentGeneticSequence> getContentSequences() {
        contents.fetch();
        return contents;
    }
}

class FilterSequenceCollection extends AbstractFolder {
    public FilterSequenceCollection(FolderRecord record) {
        super(record);
    }

    @Override
    public SequenceCollection getContentSequences() {
        // TODO Auto-generated method stub
        return null;
    }
}