package jp.scid.bio.store.folder;

import java.util.List;

import javax.swing.event.ChangeListener;

import jp.scid.bio.store.SequenceLibrary.ChangeEventSupport;
import jp.scid.bio.store.base.AbstractRecordModel;
import jp.scid.bio.store.base.PersistentListModel;
import jp.scid.bio.store.folder.FolderRecordGroupFolder.Source;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.SequenceCollection;

public abstract class AbstractFolder extends AbstractRecordModel<FolderRecord> implements Folder {
    final Source source;
    final FolderSequenceCollection sequences;
    private FoldersContainer parent;
    private final ChangeEventSupport sequencesChangeSupport;
    
    AbstractFolder(FolderRecord record, Source source) {
        super(record);
        
        if (source == null) throw new IllegalArgumentException("source must not be null");
        this.source = source;
        
        sequences = new FolderSequenceCollection();
        sequencesChangeSupport = new ChangeEventSupport(this);
    }
    
    public FoldersContainer getParent() {
        return parent;
    }
    
    public void setParent(FoldersContainer newParent) {
        if (newParent == null) throw new IllegalArgumentException("newParent must not be null");
        
        parent.removeContentFolder(this);
        newParent.addContentFolder(this);

        Long newParentId = newParent instanceof Folder ? ((Folder) newParent).id() : null;
        record.setParentId(newParentId);
        
        firePropertyChange("parent", this.parent, this.parent = newParent);
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
    public boolean delete() {
        this.parent = null;
        return super.delete();
    }
    
    @Override
    public void deleteFromParent() {
        parent.removeContentFolder(this);
        delete();
    }
    
    @Override
    public String toString() {
        return record.getName();
    }
    
    @Override
    public List<FolderContentGeneticSequence> getGeneticSequences() {
        return source.retrieveFolderContents(id());
    }
    
    public void addSequencesChangeListener(ChangeListener listener) {
        sequencesChangeSupport.addChangeListener(listener);
    }
    
    public void removeSequencesChangeListener(ChangeListener listener) {
        sequencesChangeSupport.removeChangeListener(listener);
    }
    
    protected void fireSequencesChange() {
        sequencesChangeSupport.fireStateChange();
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