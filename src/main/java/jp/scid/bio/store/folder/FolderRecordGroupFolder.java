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

    @Override
    public void moveChildTo(int childIndex, GroupFolder dest) {
        Folder child = childFolders.removeElementAt(childIndex);
        dest.addChild(child);
        child.save();
    }

    public void addChild(Folder folder) {
        folder.setParentId(id());
        childFolders.add(folder);
        
        folder.save();
    }
    
    public Folder addChildFolder(CollectionType type) {
        Folder folder = source.createFolder(type, id());
        childFolders.add(folder);
        folder.save();
        return folder;
    }
    
    @Override
    public FolderList getChildFolders() {
        return childFolders;
    }

    public static interface Source {
        FolderContentGeneticSequence createFolderContent(GeneticSequence sequence, Folder folder);
        
        GeneticSequence createGeneticSequence(File file) throws IOException, ParseException;
        
        Folder createFolder(CollectionType type, Long parentFolderId);
        
        List<Folder> retrieveFolderChildren(GroupFolder parent);
        
        List<FolderContentGeneticSequence> retrieveFolderContents(long folderId);
    }
    
    private class GroupFolderChildren extends AbstractRecordListModel<Folder> implements FolderList {
        @Override
        protected List<Folder> retrieve() {
            return source.retrieveFolderChildren(FolderRecordGroupFolder.this);
        }
    }
}
