package jp.scid.bio.store.folder;

import jp.scid.bio.store.folder.AbstractFolder.Source;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;

class FolderRecordFilterFolder extends AbstractFolder {
    public FolderRecordFilterFolder(FolderRecord record, AbstractFolder.Source source) {
        super(record, source);
    }
}