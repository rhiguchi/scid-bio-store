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
public class GeneticSequenceRecord extends org.jooq.impl.UpdatableRecordImpl<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord> {

	private static final long serialVersionUID = -66063059;

	/**
	 * 識別子
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public void setId(java.lang.Long value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.ID, value);
	}

	/**
	 * 識別子
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public java.lang.Long getId() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.ID);
	}

	/**
	 * 識別子
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public java.util.List<jp.scid.bio.store.jooq.tables.records.CollectionItemRecord> fetchCollectionItemList() {
		return create()
			.selectFrom(jp.scid.bio.store.jooq.tables.CollectionItem.COLLECTION_ITEM)
			.where(jp.scid.bio.store.jooq.tables.CollectionItem.COLLECTION_ITEM.GENETIC_SEQUENCE_ID.equal(getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.ID)))
			.fetch();
	}

	/**
	 * 名前
	 */
	public void setName(java.lang.String value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.NAME, value);
	}

	/**
	 * 名前
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
	public void setSource(java.lang.String value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.SOURCE, value);
	}

	/**
	 * ソース
	 */
	public java.lang.String getSource() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.SOURCE);
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
	 * 配列単位 : 0: 不明, 1: bp, 2: aa
	 */
	public void setUnit(java.lang.Short value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.UNIT, value);
	}

	/**
	 * 配列単位 : 0: 不明, 1: bp, 2: aa
	 */
	public java.lang.Short getUnit() {
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
	 * ファイル形式 : 0: 不明, 1: GenBank, 2: FASTA
	 */
	public void setFileType(java.lang.Short value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.FILE_TYPE, value);
	}

	/**
	 * ファイル形式 : 0: 不明, 1: GenBank, 2: FASTA
	 */
	public java.lang.Short getFileType() {
		return getValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.FILE_TYPE);
	}

	/**
	 * ファイル保管場所URI : 相対パスの時は、ライブラリをルートとする
	 */
	public void setFileUri(java.lang.String value) {
		setValue(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.FILE_URI, value);
	}

	/**
	 * ファイル保管場所URI : 相対パスの時は、ライブラリをルートとする
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
