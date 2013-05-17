package jp.scid.bio.store.folder;

import jp.scid.bio.store.folder.FolderRecordGroupFolder.Source;
import jp.scid.bio.store.jooq.tables.records.FolderRecord;

class FolderRecordFilterFolder extends AbstractFolder {
    public FolderRecordFilterFolder(FolderRecord record, Source source) {
        super(record, source);
    }
}