package jp.scid.bio.store.folder;

import java.util.List;

import jp.scid.bio.store.base.AbstractRecordModel;
import jp.scid.bio.store.base.PersistentListModel;
import jp.scid.bio.store.folder.FolderRecordGroupFolder.Source;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.SequenceCollection;

abstract class AbstractFolder extends AbstractRecordModel<FolderRecord> implements Folder {
    final Source source;
    final FolderSequenceCollection sequences;
    private FoldersContainer parent;
    
    AbstractFolder(FolderRecord record, Source source) {
        super(record);
        
        if (source == null) throw new IllegalArgumentException("source must not be null");
        this.source = source;
        
        sequences = new FolderSequenceCollection();
    }
    
    public FoldersContainer getParent() {
        return parent;
    }
    
    public void setParent(FoldersContainer newParent) {
        this.parent = newParent;
    }
    
    private void updateParentId(Long newParentId) {
        record.setParentId(newParentId);
        save();
    }
    
    @Override
    protected FolderRecord createRecord() {
        return new FolderRecord();
    }
    
    @Override
    public Long id() {
        return record.getId();
    }
    
    public void setName(String newName) {
        record.setName(newName);
    }
    
    @Override
    public void deleteFromParent() {
        parent.removeContentFolder(this);
        setParent(null);
        delete();
    }
    
    public void moveTo(FoldersContainer newParent) {
        parent.removeContentFolder(this);
        setParent(newParent);
        newParent.addContentFolder(this);

        Long newParentId = newParent instanceof Folder ? ((Folder) newParent).id() : null;
        updateParentId(newParentId);
    }
    
    @Override
    public String toString() {
        return record.getName();
    }
    
    @Override
    public SequenceCollection<FolderContentGeneticSequence> getContentSequences() {
        return sequences;
    }
    
    class FolderSequenceCollection extends PersistentListModel<FolderContentGeneticSequence>
            implements SequenceCollection<FolderContentGeneticSequence> {
        @Override
        protected Long getId(FolderContentGeneticSequence element) {
            return element.id();
        }
        
        @Override
        protected List<FolderContentGeneticSequence> retrieve() {
            return source.retrieveFolderContents(id());
        }
    }
}