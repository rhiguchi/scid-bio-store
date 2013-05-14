package jp.scid.bio.store.folder;

import jp.scid.bio.store.folder.FolderList.Source;

public class FolderLists {
    public static FolderList createRootFolderList(Source source) {
        return new JooqFolderList(null);
    }
}
