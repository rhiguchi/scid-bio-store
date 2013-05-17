package jp.scid.bio.store.folder;

import static jp.scid.bio.store.jooq.Tables.*;

import java.util.List;

import jp.scid.bio.store.base.AbstractRecordListModel;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.FolderContentGeneticSequence;
import jp.scid.bio.store.sequence.SequenceCollection;

import org.jooq.RecordMapper;
import org.jooq.Result;

public class FolderRecordGroupFolder extends AbstractFolder implements GroupFolder {
    private final GroupFolderChildren childFolders;
    
    public FolderRecordGroupFolder(FolderRecord record) {
        super(record);
        
        childFolders = new GroupFolderChildren();
    }

    @Override
    public SequenceCollection<FolderContentGeneticSequence> getContentSequences() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void moveChildFrom(GroupFolder list, int index) {
        long newParentId = id();
        if (isDescend(list.id(), newParentId)) {
            throw new IllegalArgumentException("cannot move");
        }

        Folder folder = list.removeChild(index);
        folder.setParentId(newParentId);
        childFolders.add(folder);

        folder.save();
    }

    public Folder addChild(CollectionType type) {
        if (type == null) throw new IllegalArgumentException("type must not be null");
        
        Folder folder = AbstractFolder.newFolderOf(type);
        folder.setName(getNewFolderName(type));
        childFolders.add(folder);
        folder.save();
        
        return folder;
    }

    @Override
    public Folder removeChild(int index) {
        return childFolders.remove(index);
    }
    
    String getNewFolderName(CollectionType type) {
        return "New " + type.name(); // TODO
    }
    
    boolean isDescend(Long folderId, Long folderId2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public FolderList getChildFolders() {
        return childFolders;
    }

    private class GroupFolderChildren extends AbstractRecordListModel<Folder>
            implements FolderList, RecordMapper<FolderRecord, Folder> {

        @Override
        protected List<Folder> retrieve() {
            Result<FolderRecord> result = create().selectFrom(Tables.FOLDER)
                    .where(FOLDER.PARENT_ID.eq(parentId()))
                    .fetch();
            return result.map(this);
        }

        @Override
        public final Folder map(FolderRecord record) {
            CollectionType type = CollectionType.fromRecordValue(record.getType());
            return type.createSequenceCollection(record);
        }
    }
}