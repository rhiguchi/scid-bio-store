package jp.scid.bio.store.sequence;

/**
 * 配列の単位です
 * 
 * @author HIGUCHI Ryusuke
 */
public enum SequenceUnit {
    /** 不明な配列の単位 */
    UNKNOWN((short) 0, ""),
    /** 塩基配列の単位 */
    BASE_PAIR((short) 1, "bp"),
    /** アミノ酸配列の単位 */
    AMINO_ACID((short) 2, "aa");
    
    private final short index;
    private final String label;

    private SequenceUnit(short index, String label) {
        this.index = index;
        this.label = label;
    }

    /**
     * データベース格納用の値を返します。
     * @return 数値
     */
    short dbValue() {
        return index;
    }
    
    /**
     * 文字列から SequenceUnit オブジェクトに変換します。
     * 
     * @param label 文字列
     * @return SequenceUnit オブジェクト。不明のときは {@value SequenceUnit#UNKNOWN} 。
     */
    public static SequenceUnit fromLabel(String label) {
        if (BASE_PAIR.label.equals(label)) {
            return BASE_PAIR;
        }
        else if (AMINO_ACID.label.equals(label)) {
            return AMINO_ACID;
        }
        return UNKNOWN;
    }

    /**
     * データベースに格納された値に対応する配列の単位を返します。
     * 
     * @param number データベースの値
     * @return 配列の単位
     */
    static SequenceUnit fromDbValue(short number) {
        return values()[number];
    }
}