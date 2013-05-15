package jp.scid.bio.store.base;

import javax.swing.ListModel;

@Deprecated
public interface RecordListModel<E extends RecordModel<?>> extends ListModel {
    void fetch();
}
