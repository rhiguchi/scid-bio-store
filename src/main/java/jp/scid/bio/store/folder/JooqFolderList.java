package jp.scid.bio.store.folder;

import static jp.scid.bio.store.jooq.Tables.*;

import java.util.List;

import org.jooq.Condition;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.impl.Factory;

import jp.scid.bio.store.base.AbstractRecordListModel;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;

public class JooqFolderList extends AbstractRecordListModel<Folder> implements FolderList {
    private final Long folderId;
    private final Factory create;
    private final RecordMapper<FolderRecord, Folder> mapper = new FolderMapper();
    
    private JooqFolderList(Factory factory, Long folderId) {
        this.create = factory;
        this.folderId = folderId;
    }
    
    public JooqFolderList(Factory factory, long folderId) {
        this(factory, Long.valueOf(folderId));
    }
    
    public JooqFolderList(Factory factory) {
        this(factory, null);
    }
    
    public Folder add(CollectionType type) {
        Folder folder = AbstractFolder.newFolderOf(type);
        folder.setName(getNewFolderName(type));
        add(folder);
        return folder;
    }
    
    @Override
    protected List<Folder> retrieve() {
        return findChildFolders(folderId);
    }

    @Override
    public void moveChildFrom(FolderList list, int index) {
        if (isDescend(list.folderId(), folderId)) {
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

    String getNewFolderName(CollectionType type) {
        return "New " + type.name(); // TODO
    }
    
    boolean isDescend(Long folderId, Long folderId2) {
        // TODO Auto-generated method stub
        return false;
    }

    List<Folder> findChildFolders(Long parentId) {
        Condition parentFolderCondition = parentId == null ? FOLDER.PARENT_ID.isNull()
            : FOLDER.PARENT_ID.eq(parentId);
        Result<FolderRecord> result = create.selectFrom(Tables.FOLDER)
                .where(parentFolderCondition)
                .fetch();
        
        return result.map(mapper);
    }

    private static class FolderMapper implements RecordMapper<FolderRecord, Folder> {
        @Override
        public final Folder map(FolderRecord record) {
            CollectionType type = CollectionType.fromRecordValue(record.getType());
            return type.createSequenceCollection(record);
        }
    }
}