/**
 * This class is generated by jOOQ
 */
package jp.scid.bio.store.jooq.tables.records;

/**
 * This class is generated by jOOQ.
 *
 * 配列 : バイオデータファイルを表すテーブル
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class GeneticSequenceRecord extends org.jooq.impl.TableRecordImpl<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord> {

	private static final long serialVersionUID = -461276065;

	/**
	 * 展示物識別番号
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public void setId(java.lang.Long value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.ID, value);
	}

	/**
	 * 展示物識別番号
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public java.lang.Long getId() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.ID);
	}

	/**
	 * 名前 : 名前
	 */
	public void setName(java.lang.String value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.NAME, value);
	}

	/**
	 * 名前 : 名前
	 */
	public java.lang.String getName() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.NAME);
	}

	/**
	 * 配列長
	 */
	public void setLength(java.lang.Integer value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.LENGTH, value);
	}

	/**
	 * 配列長
	 */
	public java.lang.Integer getLength() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.LENGTH);
	}

	/**
	 * アクセッション番号
	 */
	public void setAccession(java.lang.String value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.ACCESSION, value);
	}

	/**
	 * アクセッション番号
	 */
	public java.lang.String getAccession() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.ACCESSION);
	}

	/**
	 * 名前空間
	 */
	public void setNamespace(java.lang.String value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.NAMESPACE, value);
	}

	/**
	 * 名前空間
	 */
	public java.lang.String getNamespace() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.NAMESPACE);
	}

	/**
	 * バージョン番号
	 */
	public void setVersion(java.lang.Integer value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.VERSION, value);
	}

	/**
	 * バージョン番号
	 */
	public java.lang.Integer getVersion() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.VERSION);
	}

	/**
	 * 定義
	 */
	public void setDefinition(java.lang.String value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.DEFINITION, value);
	}

	/**
	 * 定義
	 */
	public java.lang.String getDefinition() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.DEFINITION);
	}

	/**
	 * ソース
	 */
	public void setSourceText(java.lang.String value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.SOURCE_TEXT, value);
	}

	/**
	 * ソース
	 */
	public java.lang.String getSourceText() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.SOURCE_TEXT);
	}

	/**
	 * 生物種
	 */
	public void setOrganism(java.lang.String value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.ORGANISM, value);
	}

	/**
	 * 生物種
	 */
	public java.lang.String getOrganism() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.ORGANISM);
	}

	/**
	 * 更新年月日
	 */
	public void setDate(java.sql.Date value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.DATE, value);
	}

	/**
	 * 更新年月日
	 */
	public java.sql.Date getDate() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.DATE);
	}

	/**
	 * 配列単位
	 */
	public void setUnit(java.lang.String value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.UNIT, value);
	}

	/**
	 * 配列単位
	 */
	public java.lang.String getUnit() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.UNIT);
	}

	/**
	 * 分子型
	 */
	public void setMoleculeType(java.lang.String value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.MOLECULE_TYPE, value);
	}

	/**
	 * 分子型
	 */
	public java.lang.String getMoleculeType() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.MOLECULE_TYPE);
	}

	/**
	 * ファイル形式
	 */
	public void setFileType(java.lang.Integer value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.FILE_TYPE, value);
	}

	/**
	 * ファイル形式
	 */
	public java.lang.Integer getFileType() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.FILE_TYPE);
	}

	/**
	 * ファイル保管場所URI
	 */
	public void setFileUri(java.lang.String value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.FILE_URI, value);
	}

	/**
	 * ファイル保管場所URI
	 */
	public java.lang.String getFileUri() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.FILE_URI);
	}

	/**
	 * Create a detached GeneticSequenceRecord
	 */
	public GeneticSequenceRecord() {
		super(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE);
	}
}