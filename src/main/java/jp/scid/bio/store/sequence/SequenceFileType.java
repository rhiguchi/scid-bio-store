package jp.scid.bio.store.sequence;

public enum SequenceFileType {
    UNKNOWN(0),
    GENBANK(1, ".gbk"),
    FASTA(2, ".fasta"),
    ;
    
    private final int number;
    private final String defaultExtension;
    
    private SequenceFileType(int number, String defaultExtension) {
        this.number = number;
        this.defaultExtension = defaultExtension;
    }
    private SequenceFileType(int number) {
        this(number, ".txt");
    }
    
    public short dbValue() {
        return (short) number;
    }
    
    public String defaultExtension() {
        return defaultExtension;
    }
    
    public static SequenceFileType fromDbValue(short number) {
        return values()[number];
    }
}