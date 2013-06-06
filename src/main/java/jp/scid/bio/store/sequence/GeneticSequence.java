package jp.scid.bio.store.sequence;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;

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
     * @return 元データファイルの場所。ローカルファイルにないときは {@code null} 。
     */
    File getFile();

    /**
     * 名前を返します。
     * 
     * @return 名前
     */
    String name();

    /**
     * 塩基配列の長さを返します。
     * 
     * @return　塩基配列の長さ
     */
    int length();

    /**
     * アクセッション番号を返します。
     * 
     * @return アクセッション番号
     */
    String accession();

    /**
     * バージョン番号を返します。
     * 
     * @return バージョン番号。不明なときは {@code null}
     */
    Integer version();

    /**
     * 定義記述を返します。
     * 
     * @return 定義
     */
    String definition();

    /**
     * 由来記述を返します。
     * 
     * @return ソース
     */
    String source();

    /**
     * 生物種を返します。
     * 
     * @return 生物種
     */
    String organism();

    /**
     * 最終更新日付を返します。
     * 
     * @return 更新日付
     */
    Date date();

    /**
     * 配列の単位を返します。
     * 
     * @return 単位
     */
    SequenceUnit sequenceUnit();

    /**
     * 配列の名前空間を返します。
     * 
     * @return 名前空間。
     */
    String namespace();
    
    /**
     * 配列の種類を返します。
     * 
     * @return 種類
     */
    String moleculeType();
    
    /**
     * ファイルの種類を返します。
     * 
     * @return ファイルの種類
     */
    SequenceFileType sequenceFileType();
}
