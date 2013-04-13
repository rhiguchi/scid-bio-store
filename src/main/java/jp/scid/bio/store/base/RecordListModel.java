package jp.scid.bio.store.base;

import javax.swing.ListModel;

public interface RecordListModel<E extends RecordModel<?>> extends ListModel {
    E findElement(long id);
    
    void fetch();
    
    E remove(int index);
    
    void add(E element);
    
    void add(int index, E element);
}
