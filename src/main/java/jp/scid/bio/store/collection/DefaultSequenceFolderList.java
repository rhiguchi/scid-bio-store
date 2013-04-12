package jp.scid.bio.store.collection;

import java.util.List;

import jp.scid.bio.store.base.PersistentListModel;
import jp.scid.bio.store.base.RecordListModel;
import jp.scid.bio.store.element.CollectionType;
import jp.scid.bio.store.element.SequenceFolder;
import jp.scid.bio.store.jooq.Tables;

public class DefaultSequenceFolderList {
    private final Long folderId;
    private final Source source;
    
    public DefaultSequenceFolderList(Source source, Long folderId) {
        this.source = source;
        this.folderId = folderId;
    }
    
    public SequenceFolder add(CollectionType type) {
        SequenceFolder folder = SequenceFolder.newFolderOf(type);
        folder.setName(source.getNewFolderName(type));
        add(folder);
        return folder;
    }

    @Override
    protected List<SequenceFolder> retrieve() {
        return source.findChildFolders(folderId);
    }

    @Override
    public void moveChildFrom(SequenceFolderList list, int index) {
        // TODO Auto-generated method stub
        
    }
}