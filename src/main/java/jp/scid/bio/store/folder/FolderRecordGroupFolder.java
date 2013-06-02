package jp.scid.bio.store.folder;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import jp.scid.bio.store.base.AbstractRecordListModel;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;

public class FolderRecordGroupFolder extends AbstractFolder implements FoldersContainer {
    private final GroupFolderChildren childFolders;
    
    public FolderRecordGroupFolder(FolderRecord record, Source source) {
        super(record, source);
        
        childFolders = new GroupFolderChildren();
    }

    public Folder createContentFolder(CollectionType type) {
        Folder folder = source.createFolder(type, id());
        childFolders.add(folder);
        return folder;
    }
    
    @Override
    public FolderList getContentFolders() {
        return childFolders;
    }
    
    public boolean removeContentFolder(Folder folder) {
        return childFolders.removeElement(folder);
    }
    
    public void addContentFolder(Folder folder) {
        childFolders.addElement(folder);
    }

    @Override
    public Folder removeContentFolderAt(int index) {
        return childFolders.removeElementAt(index);
    }
    
    public static interface Source {
        FolderContentGeneticSequence createFolderContent(GeneticSequence sequence, Folder folder);
        
        GeneticSequence createGeneticSequence(File file) throws IOException, ParseException;
        
        Folder createFolder(CollectionType type, Long parentFolderId);
        
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
