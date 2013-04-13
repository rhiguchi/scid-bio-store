package jp.scid.bio.store.folder;

import java.util.List;

import jp.scid.bio.store.base.AbstractRecordListModel;
import jp.scid.bio.store.base.RecordListModel;

public interface FolderList extends RecordListModel<Folder> {
    long folderId();
    
    public static interface Source {
        List<Folder> findChildFolders(Long parentFolderId);

        String getNewFolderName(CollectionType type);
        
        boolean isDescend(Long folderId, Long folderId2);
    }
}

class FolderListImpl extends AbstractRecordListModel<Folder> implements FolderList {
    private final Long folderId;
    private final Source source;
    
    FolderListImpl(Source source, Long folderId) {
        this.source = source;
        this.folderId = folderId;
    }
    
    public Folder add(CollectionType type) {
        Folder folder = AbstractFolder.newFolderOf(type);
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
        if (source.isDescend(list.folderId(), folderId)) {
            throw new IllegalArgumentException("cannot move");
        }
        
        Folder folder = list.remove(index);
        folder.setParentId(folderId);
        add(folder);
        
        folder.save();
    }

    @Override
    public long folderId() {
        return folderId;
    }
}