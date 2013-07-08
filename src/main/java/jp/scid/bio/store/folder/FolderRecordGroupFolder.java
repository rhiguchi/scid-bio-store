package jp.scid.bio.store.folder;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.swing.event.ChangeListener;

import jp.scid.bio.store.SequenceLibrary.ChangeEventSupport;
import jp.scid.bio.store.base.AbstractRecordListModel;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;

public class FolderRecordGroupFolder extends AbstractFolder implements FoldersContainer {
    private final GroupFolderChildren childFolders;
    private final ChangeEventSupport folrdersChangeSupport;
    
    public FolderRecordGroupFolder(FolderRecord record, Source source) {
        super(record, source);
        
        childFolders = new GroupFolderChildren();
        folrdersChangeSupport = new ChangeEventSupport(this);
    }

    @Override
    public void addFoldersChangeListener(ChangeListener listener) {
        folrdersChangeSupport.addChangeListener(listener);
    }
    
    @Override
    public void removeFoldersChangeListener(ChangeListener listener) {
        folrdersChangeSupport.removeChangeListener(listener);
    }
    
    @Override
    public Iterable<Folder> getFolders() {
        return source.retrieveFolderChildren(this, id());
    }
    
    public Folder createChildFolder(CollectionType type) {
        return source.createFolder(type, id(), this);
    }
    
    public Folder createContentFolder(CollectionType type) {
        Folder folder = source.createFolder(type, id(), this);
        folder.save();
        childFolders.add(folder);
        return folder;
    }
    
    @Override
    public FolderList getContentFolders() {
        return childFolders;
    }
    
    public boolean removeContentFolder(Folder folder) {
        try {
            return childFolders.removeElement(folder);
        }
        finally {
            folrdersChangeSupport.fireStateChange();
        }
    }
    
    public void addContentFolder(Folder folder) {
        try {
            childFolders.addElement(folder);
        }
        finally {
            folrdersChangeSupport.fireStateChange();
        }
    }

    @Override
    public Folder removeContentFolderAt(int index) {
        return childFolders.removeElementAt(index);
    }
    
    @Override
    public int indexOfFolder(Folder folder) {
        return childFolders.indexOf(folder);
    }
    
    public static interface Source {
        FolderContentGeneticSequence createFolderContent(GeneticSequence sequence, Folder folder);
        
        GeneticSequence createGeneticSequence(File file) throws IOException, ParseException;
        
        Folder createFolder(CollectionType type, Long parentFolderId, FoldersContainer parent);
        
        List<Folder> retrieveFolderChildren(FoldersContainer parent, long parentFolderId);
        
        List<FolderContentGeneticSequence> retrieveFolderContents(long folderId);
    }
    
    private class GroupFolderChildren extends AbstractRecordListModel<Folder> implements FolderList {
        @Override
        protected List<Folder> retrieve() {
            return source.retrieveFolderChildren(FolderRecordGroupFolder.this, id());
        }
    }
}
