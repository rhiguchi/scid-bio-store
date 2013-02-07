/**
 * This class is generated by jOOQ
 */
package jp.scid.bio.store.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 配列 : バイオデータファイルを表すテーブル
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class GeneticSequence extends org.jooq.impl.UpdatableTableImpl<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord> {

	private static final long serialVersionUID = 1878241279;

	/**
	 * The singleton instance of PUBLIC.GENETIC_SEQUENCE
	 */
	public static final jp.scid.bio.store.jooq.tables.GeneticSequence GENETIC_SEQUENCE = new jp.scid.bio.store.jooq.tables.GeneticSequence();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord> getRecordType() {
		return jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord.class;
	}

	/**
	 * 識別子
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.Long> ID = createField("ID", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * 名前
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 配列長
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.Integer> LENGTH = createField("LENGTH", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * アクセッション番号
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.String> ACCESSION = createField("ACCESSION", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 名前空間
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.String> NAMESPACE = createField("NAMESPACE", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * バージョン番号
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.Integer> VERSION = createField("VERSION", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * 定義
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.String> DEFINITION = createField("DEFINITION", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * ソース
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.String> SOURCE = createField("SOURCE", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 生物種
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.String> ORGANISM = createField("ORGANISM", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 更新年月日
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.sql.Date> DATE = createField("DATE", org.jooq.impl.SQLDataType.DATE, this);

	/**
	 * 配列単位 : 0: 不明, 1: bp, 2: aa
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.Short> UNIT = createField("UNIT", org.jooq.impl.SQLDataType.SMALLINT, this);

	/**
	 * 分子型
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.String> MOLECULE_TYPE = createField("MOLECULE_TYPE", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * ファイル形式 : 0: 不明, 1: GenBank, 2: FASTA
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.Short> FILE_TYPE = createField("FILE_TYPE", org.jooq.impl.SQLDataType.SMALLINT, this);

	/**
	 * ファイル保管場所URI : 相対パスの時は、ライブラリをルートとする
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.String> FILE_URI = createField("FILE_URI", org.jooq.impl.SQLDataType.VARCHAR, this);

	public GeneticSequence() {
		super("GENETIC_SEQUENCE", jp.scid.bio.store.jooq.Public.PUBLIC);
	}

	public GeneticSequence(java.lang.String alias) {
		super(alias, jp.scid.bio.store.jooq.Public.PUBLIC, jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE);
	}

	@Override
	public org.jooq.Identity<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.Long> getIdentity() {
		return jp.scid.bio.store.jooq.Keys.IDENTITY_GENETIC_SEQUENCE;
	}

	@Override
	public org.jooq.UniqueKey<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord> getMainKey() {
		return jp.scid.bio.store.jooq.Keys.CONSTRAINT_5;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord>>asList(jp.scid.bio.store.jooq.Keys.CONSTRAINT_5);
	}

	@Override
	public jp.scid.bio.store.jooq.tables.GeneticSequence as(java.lang.String alias) {
		return new jp.scid.bio.store.jooq.tables.GeneticSequence(alias);
	}
}
