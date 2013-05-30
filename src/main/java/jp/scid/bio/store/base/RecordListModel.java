package jp.scid.bio.store.base;

import java.util.List;

import javax.swing.ListModel;

public class RecordListModel<E extends RecordModel> extends PersistentListModel<E> implements ListModel {
    private final Source<E> source;
    
    public RecordListModel(Source<E> source) {
        if (source == null) throw new IllegalArgumentException("source must not be null");
        
        this.source = source;
    }

    @Override
    protected Long getId(E element) {
        return element.id();
    }

    @Override
    protected List<E> retrieve() {
        return source.retrieveRecords();
    }
    
    public static interface Source<E extends RecordModel> {
        List<E> retrieveRecords();
    }
}
