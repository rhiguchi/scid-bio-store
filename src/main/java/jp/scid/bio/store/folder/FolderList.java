package jp.scid.bio.store.folder;

import java.util.List;

import jp.scid.bio.store.base.RecordListModel;

public class FolderList extends RecordListModel<Folder> {
    private final Long folderId;
    private final Source source;
    
    FolderList(Source source, Long folderId) {
        this.source = source;
        this.folderId = folderId;
    }
    
    public static FolderList createRootFolderList(Source source) {
        return new FolderList(source, null);
    }
    
    public Folder add(CollectionType type) {
        Folder folder = Folder.newFolderOf(type);
        folder.setName(source.getNewFolderName(type));
        add(folder);
        return folder;
    }
    
    @Override
    public Folder remove(int index) {
        return super.remove(index);
    }
    
    @Override
    protected List<Folder> retrieve() {
        return source.findChildFolders(folderId);
    }

    public void moveChildFrom(FolderList list, int index) {
        if (source.isDescend(list.folderId, folderId)) {
            throw new IllegalArgumentException("cannot move");
        }
        
        Folder folder = list.removeFromInternalList(index);
        folder.setParentId(folderId);
        insertIntoStore(folder);
        
        folder.save();
    }

    public static interface Source {
        List<Folder> findChildFolders(Long parentFolderId);

        String getNewFolderName(CollectionType type);
        
        boolean isDescend(Long folderId, Long folderId2);
    }
}