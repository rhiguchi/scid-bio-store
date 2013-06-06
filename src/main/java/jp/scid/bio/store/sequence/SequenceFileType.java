package jp.scid.bio.store.sequence;

/**
 * 配列ファイルの書式型です。
 * 
 * @author HIGUCHI Ryusuke
 */
public enum SequenceFileType {
    /** 形式不明のファイル */
    UNKNOWN(0),
    /** GenBank 書式のファイル */
    GENBANK(1, ".gbk"),
    /** Fasta 書式のファイル */
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

    /**
     * データベース格納用の値を返します。
     * @return 数値
     */
    short dbValue() {
        return (short) number;
    }
    
    /**
     * ファイルの標準的な拡張子を返します。
     * 
     * @return ピリオドから始まる拡張子文字列
     */
    String defaultExtension() {
        return defaultExtension;
    }
    
    /**
     * データベースに格納された値に対応する配列の単位を返します。
     * 
     * @param number データベースの値
     * @return 配列の単位
     */
    static SequenceFileType fromDbValue(short number) {
        return values()[number];
    }
}