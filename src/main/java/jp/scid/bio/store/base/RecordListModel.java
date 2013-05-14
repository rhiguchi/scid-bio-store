package jp.scid.bio.store.base;

import javax.swing.ListModel;

public interface RecordListModel<E extends RecordModel<?>> extends ListModel {
    void fetch();
}
