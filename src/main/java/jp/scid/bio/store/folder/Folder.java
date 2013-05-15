package jp.scid.bio.store.folder;


import jp.scid.bio.store.base.AbstractRecordModel;
import jp.scid.bio.store.base.RecordModel;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.SequenceCollection;

import org.jooq.impl.Factory;

/**
 * シーケンスデータを格納したフォルダの構造定義
 * @author higuchi
 *
 */
public interface Folder extends RecordModel {
    /**
     * 親フォルダの id を返します。
     * @return 親フォルダの id。ルートフォルダで親がいないときは {@code null} 。
     */
    Long parentId();
    
    /**
     * 親フォルダの id を設定します。
     * @param newParentId 新しい親のフォルダ id。親のいないルートフォルダにするときは {@code null} 。
     */
    void setParentId(Long newParentId);
    
    /**
     * このフォルダの名前を設定します。
     * 
     * @param newName 新しい名前
     */
    void setName(String newName);

    /**
     * このフォルダにある配列情報を返します。
     * 
     * @return 配列情報
     */
    SequenceCollection<FolderContentGeneticSequence> getContentSequences();
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
    
    public void setName(String newName) {
        record.setName(newName);
    }
    
    Factory create() {
        return (Factory) record.getConfiguration();
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