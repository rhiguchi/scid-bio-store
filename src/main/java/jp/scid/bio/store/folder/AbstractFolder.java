package jp.scid.bio.store.folder;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.swing.event.ChangeListener;

import jp.scid.bio.store.base.AbstractRecordModel;
import jp.scid.bio.store.base.ChangeEventSupport;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;

public abstract class AbstractFolder extends AbstractRecordModel<FolderRecord> implements Folder {
    final AbstractFolder.Source source;
    private FoldersContainer parent;
    private final ChangeEventSupport sequencesChangeSupport;
    
    AbstractFolder(FolderRecord record, AbstractFolder.Source source) {
        super(record);
        
        if (source == null) throw new IllegalArgumentException("source must not be null");
        this.source = source;
        
        sequencesChangeSupport = new ChangeEventSupport(this);
    }
    
    public FoldersContainer getParent() {
        return parent;
    }
    
    public void setParent(FoldersContainer newParent) {
        if (newParent == null) throw new IllegalArgumentException("newParent must not be null");
        // TODO ancester check
        
        Long newParentId = newParent instanceof Folder ? ((Folder) newParent).id() : null;
        record.setParentId(newParentId);
        save();
        
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
        parent.removeChildFolder(this);
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
    
    public static interface Source {
        FolderContentGeneticSequence createFolderContent(GeneticSequence sequence, Folder folder);
        
        GeneticSequence createGeneticSequence(File file) throws IOException, ParseException;
        
        Folder createFolder(CollectionType type, Long parentFolderId, FoldersContainer parent);
        
        List<Folder> retrieveFolderChildren(FoldersContainer parent, Long parentFolderId);
        
        List<FolderContentGeneticSequence> retrieveFolderContents(long folderId);
    }
}