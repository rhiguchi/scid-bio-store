package jp.scid.bio.store.sequence;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * 配列ファイルを読み込めるシーケンスソースです
 * @author ryusuke
 *
 */
public interface ImportableSequenceSource extends GeneticSequenceSource {
    /**
     * ファイルから配列を読み込みます。
     * 
     * @param file 配列ファイル
     * @return 読み込まれた配列情報
     * @throws IOException 読み込みに失敗した時
     * @throws ParseException ファイルの書式が正しくない時
     */
    GeneticSequence importSequence(File file) throws IOException, ParseException;
    
//    /**
//     * 配列情報を、ソースファイルを指定して作成します。
//     * 
//     * @param file 
//     * @return 配列情報
//     */
//    GeneticSequence createFromFile(File file);
//
//    /**
//     * 配列情報を追加します
//     * 
//     * @param geneticSequence
//     */
//    void addGeneticSequence(GeneticSequence geneticSequence);
}
