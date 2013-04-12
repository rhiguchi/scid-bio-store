package jp.scid.bio.store.collection;

import static org.jooq.impl.Factory.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.CollectionItemRecord;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.Factory;

abstract class AbstractFolderSequenceCollection extends AbstractSequenceCollection {
    final long folderId;
    
    public AbstractFolderSequenceCollection(Factory factory, long folderId) {
        super(factory);
        
        this.folderId = folderId;
    }
}

class FilterSequenceCollection extends AbstractFolderSequenceCollection {
    
    public FilterSequenceCollection(Factory factory, long folderId) {
        super(factory, folderId);
    }
    
    @Override
    protected List<IdentifiableRecord> retrieve() {
        // TODO Auto-generated method stub
        return Collections.emptyList();
    }
    
}
