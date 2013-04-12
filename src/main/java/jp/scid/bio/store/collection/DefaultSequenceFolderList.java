package jp.scid.bio.store.collection;

import java.util.List;

import jp.scid.bio.store.base.PersistentListModel;
import jp.scid.bio.store.element.CollectionType;
import jp.scid.bio.store.element.SequenceFolder;
import jp.scid.bio.store.jooq.Tables;

public class DefaultSequenceFolderList extends PersistentListModel<SequenceFolder> implements SequenceFolderList {
    private final Long folderId;
    private final Source source;
    
    public DefaultSequenceFolderList(Source source, Long folderId) {
        this.source = source;
        this.folderId = folderId;
    }
    
    @Override
    public SequenceFolder remove(int index) {
        return super.remove(index);
    }
    
    public SequenceFolder add(CollectionType type) {
        SequenceFolder folder = SequenceFolder.newFolderOf(type);
        folder.setName(source.getNewFolderName(type));
        add(folder);
        return folder;
    }
    
    @Override
    public void add(SequenceFolder element) {
        element.setParentId(folderId);
        super.add(element);
    }

    @Override
    protected List<SequenceFolder> retrieve() {
        return source.findChildFolders(folderId);
    }

    @Override
    protected Long getId(SequenceFolder element) {
        return element.id();
    }

    @Override
    protected boolean deleteFromStore(SequenceFolder folder) {
        return folder.delete();
    }

    @Override
    protected boolean insertIntoStore(SequenceFolder folder) {
        folder.setValue(Tables.FOLDER.ID, null);
        return folder.store();
    }

    @Override
    protected boolean update(SequenceFolder element) {
        return element.store();
    }

    @Override
    public void moveChildFrom(SequenceFolderList list, int index) {
        // TODO Auto-generated method stub
        
    }
}