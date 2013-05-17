package jp.scid.bio.store.folder;

import java.util.List;

import jp.scid.bio.store.base.PersistentListModel;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;

public class FolderRecordGroupFolder extends AbstractFolder implements GroupFolder {
    private final GroupFolderChildren childFolders;
    
    public FolderRecordGroupFolder(FolderRecord record, Source source) {
        super(record, source);
        
        childFolders = new GroupFolderChildren();
    }

    @Override
    public void moveChildTo(int childIndex, GroupFolder dest) {
        Folder child = childFolders.remove(childIndex);
        dest.addChild(child);
        child.save();
    }

    public void addChild(Folder folder) {
        folder.setParentId(id());
        childFolders.add(folder);
        
        folder.save();
    }
    
    public Folder addNewChild(CollectionType type) {
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
        Folder createFolder(CollectionType type, Long parentFolderId);
        
        List<Folder> retrieveFolderChildren(long parentFolderId);
        
        List<FolderContentGeneticSequence> retrieveFolderContents(long folderId);
    }
    
    private class GroupFolderChildren extends PersistentListModel<Folder> implements FolderList {
        @Override
        protected Long getId(Folder element) {
            return element.id();
        }
        @Override
        protected List<Folder> retrieve() {
            return source.retrieveFolderChildren(id());
        }
    }
}
