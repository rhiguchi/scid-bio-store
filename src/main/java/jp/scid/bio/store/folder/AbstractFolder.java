package jp.scid.bio.store.folder;

import java.util.List;

import jp.scid.bio.store.base.AbstractRecordModel;
import jp.scid.bio.store.base.PersistentListModel;
import jp.scid.bio.store.folder.FolderRecordGroupFolder.Source;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.SequenceCollection;

import org.jooq.impl.Factory;

abstract class AbstractFolder extends AbstractRecordModel<FolderRecord> implements Folder {
    final Source source;
    final FolderSequenceCollection sequences;
    private GroupFolder parent;
    
    AbstractFolder(FolderRecord record, Source source) {
        super(record);
        
        if (source == null) throw new IllegalArgumentException("source must not be null");
        this.source = source;
        
        sequences = new FolderSequenceCollection();
    }
    
    @Override
    public GroupFolder getParent() {
        return parent;
    }
    
    public void setParent(GroupFolder parent) {
        if (parent != null && parent.id() == null) {
            throw new IllegalArgumentException("need parent id");
        }
        Long newParentId = parent == null ? null : parent.id();
        record.setParentId(newParentId);
        
        this.parent = parent;
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