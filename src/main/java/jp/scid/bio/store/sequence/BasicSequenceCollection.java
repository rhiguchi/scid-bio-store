package jp.scid.bio.store.sequence;

import static org.jooq.impl.Factory.*;

import java.util.List;

import jp.scid.bio.store.folder.BasicSequenceFolder;

import org.jooq.Field;

public class BasicSequenceCollection extends AbstractMutableSequenceCollection<FolderContentGeneticSequence> {
    final static Field<Long> LAST_MODIFICATION = fieldByName(Long.class, "last_modification");
    final static Field<String> TABLE_NAME = fieldByName(String.class, "table_name");
    
    private final BasicSequenceFolder owner;
    
    public BasicSequenceCollection(BasicSequenceFolder owner) {
        this.owner = owner;
    }
    
    @Override
    protected List<FolderContentGeneticSequence> retrieve() {
        return owner.retrieveFolderContent();
    }
    
    @Override
    public void addSequence(DefaultGeneticSequence sequence) {
        if (sequence == null) throw new IllegalArgumentException("sequence must not be null");
        
        FolderContentGeneticSequence content = owner.addSequence(sequence);
        add(content);
    }
}