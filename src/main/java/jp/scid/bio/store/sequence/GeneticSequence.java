package jp.scid.bio.store.sequence;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;

import jp.scid.bio.store.FileLibrary.SequenceFileType;
import jp.scid.bio.store.FileLibrary.SequenceUnit;
import jp.scid.bio.store.base.RecordModel;

/**
 * 塩基配列情報です。
 * 
 * @author higuchi
 *
 */
public interface GeneticSequence extends RecordModel {
    /**
     * 配列情報を元ファイルから再読み込みをします。
     * 
     * 元ファイルは {@link #getFile()} で得られるファイルパスから読み込まれます。
     * 
     * @throws IOException 元ファイルを正常に読み込むことができなかったとき
     * @throws ParseException 元ファイルの配列情報が正しい書式ではなかったとき
     */
    void reload() throws IOException, ParseException;
    
    /**
     * 元ファイルをライブラリ内の専用格納場所へ格納します。
     * 元ファイルの場所が変わります。
     * 
     * ファイルがすでに格納場所にあるときは、なにもしません。
     * 
     * @return 元ファイルの場所が変わったときは {@code true} 。
     *         元ファイルが {@code null} 、もしくはすでに格納場所内にあるなど、
     *         ファイルの場所が変わらなかったときは {@code false} 。
     * @throws IOException ファイルを正常に格納できなかったとき
     */
    boolean saveFileToLibrary() throws IOException;
    
    /**
     * この配列情報の元データの場所を返します。
     * 
     * @return 元データファイルの場所
     */
    File getFile();

    String name();

    int length();

    String accession();

    Integer version();

    String definition();

    String source();

    String organism();

    Date date();

    SequenceUnit sequenceUnit();

    String moleculeType();
    
    SequenceFileType sequenceFileType();
}
