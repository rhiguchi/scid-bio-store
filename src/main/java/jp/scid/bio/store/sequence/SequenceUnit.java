package jp.scid.bio.store.sequence;

public enum SequenceUnit {
    UNKNOWN((short) 0, ""),
    BASE_PAIR((short) 1, "bp"),
    AMINO_ACID((short) 2, "aa");
    
    private final short index;
    private final String label;

    private SequenceUnit(short index, String label) {
        this.index = index;
        this.label = label;
    }

    public short dbValue() {
        return index;
    }
    
    public static SequenceUnit fromLabel(String label) {
        if (BASE_PAIR.label.equals(label)) {
            return BASE_PAIR;
        }
        else if (AMINO_ACID.label.equals(label)) {
            return AMINO_ACID;
        }
        return UNKNOWN;
    }

    public static SequenceUnit fromDbValue(short number) {
        return values()[number];
    }
}