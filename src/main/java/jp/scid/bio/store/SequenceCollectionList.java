package jp.scid.bio.store;

import static jp.scid.bio.store.jooq.Tables.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import org.jooq.Condition;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.impl.Factory;

import jp.scid.bio.store.collection.AbstractSequenceCollection;
import jp.scid.bio.store.collection.CollectionType;
import jp.scid.bio.store.collection.SequenceCollection;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;

public interface SequenceCollectionList extends ListModel {
    @Override
    public SequenceCollection getElementAt(int index);
}

class DefaultSequenceCollectionList extends JooqTableContents<SequenceCollection> implements SequenceCollectionList, RecordMapper<FolderRecord, SequenceCollection> {
    private final Long folderId;
    
    public DefaultSequenceCollectionList(Factory factory, Long folderId) {
        super(factory);
        this.folderId = folderId;
    }

    public SequenceCollection createElement() {
        return createElement(CollectionType.BASIC);
    }
    
    public SequenceCollection createElement(CollectionType type) {
        FolderRecord record = create.newRecord(Tables.FOLDER);
        record.setId(null);
        record.setType(type.getDbValue());
        
        SequenceCollection collection = map(record);
//        collection.setName(getNewFolderName(type));
        
        return collection;
    }

    @Override
    public SequenceCollection map(FolderRecord record) {
        return null; // TODO AbstractSequenceCollection.createCollection(create, record);
    }

    protected List<SequenceCollection> retrieve() {
        Result<FolderRecord> result = create
                .selectFrom(Tables.FOLDER)
                .where(FOLDER.PARENT_ID.eq(folderId))
                .fetch();
        
        return result.map(this);
    }
    
    @Override
    public String getTableName() {
        return Tables.FOLDER.getName();
    }
    
    protected String getNewFolderName(CollectionType type) {
        return "New " + type.name(); // TODO
    }

    @Override
    protected Long getId(SequenceCollection element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean deleteFromStore(SequenceCollection element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean insertIntoStore(SequenceCollection element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean update(SequenceCollection element) {
        // TODO Auto-generated method stub
        return false;
    }
    
}