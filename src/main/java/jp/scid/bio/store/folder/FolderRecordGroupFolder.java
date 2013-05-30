package jp.scid.bio.store.folder;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import jp.scid.bio.store.base.AbstractRecordListModel;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.GeneticSequence;

public class FolderRecordGroupFolder extends AbstractFolder implements GroupFolder {
    private final GroupFolderChildren childFolders;
    
    public FolderRecordGroupFolder(FolderRecord record, Source source) {
        super(record, source);
        
        childFolders = new GroupFolderChildren();
    }

    public Folder addContentFolder(CollectionType type) {
        Folder folder = source.createFolder(type, id());
        childFolders.add(folder);
        folder.save();
        return folder;
    }
    
    public void moveInto(Folder newChild) {
        source.moveFolder(newChild, this);
    }
    
    @Override
    public FolderList getContentFolders() {
        return childFolders;
    }

    @Override
    public Folder removeContentFolderAt(int index) {
        Folder folder = childFolders.removeElementAt(index);
        folder.delete();
        return folder;
    }
    
    public static interface Source {
        FolderContentGeneticSequence createFolderContent(GeneticSequence sequence, Folder folder);
        
        GeneticSequence createGeneticSequence(File file) throws IOException, ParseException;
        
        Folder createFolder(CollectionType type, Long parentFolderId);
        
        List<Folder> retrieveFolderChildren(GroupFolder parent);
        
        List<FolderContentGeneticSequence> retrieveFolderContents(long folderId);

        void moveFolder(Folder folder, GroupFolder newParent);
    }
    
    private class GroupFolderChildren extends AbstractRecordListModel<Folder> implements FolderList {
        @Override
        protected List<Folder> retrieve() {
            return source.retrieveFolderChildren(FolderRecordGroupFolder.this);
        }
    }
}
