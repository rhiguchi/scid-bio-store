package jp.scid.bio.store.element;

import jp.scid.bio.store.jooq.tables.records.FolderRecord;
import jp.scid.bio.store.sequence.SequenceCollection;

/**
 * 子フォルダをもつフォルダ
 * @author higuchi
 *
 */
public class SequenceGroupFolder extends SequenceFolder {

    public SequenceGroupFolder(FolderRecord record) {
        super(record);
        
        if (CollectionType.fromRecordValue(record.getType()) != CollectionType.NODE) {
            throw new IllegalArgumentException("record type msut be a group");
        }
    }

//    public SequenceFolderList getChildren() {
//        DefaultSequenceFolderList children = new DefaultSequenceFolderList(getFactory(), id());
//        children.fetch();
//        return children;
//    }

    @Override
    public SequenceCollection getContentSequences() {
        // TODO Auto-generated method stub
        return null;
    }
}