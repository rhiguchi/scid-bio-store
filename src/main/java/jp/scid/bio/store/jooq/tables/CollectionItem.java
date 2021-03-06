/**
 * This class is generated by jOOQ
 */
package jp.scid.bio.store.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 配列コレクション
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class CollectionItem extends org.jooq.impl.UpdatableTableImpl<jp.scid.bio.store.jooq.tables.records.CollectionItemRecord> {

	private static final long serialVersionUID = 196924248;

	/**
	 * The singleton instance of PUBLIC.COLLECTION_ITEM
	 */
	public static final jp.scid.bio.store.jooq.tables.CollectionItem COLLECTION_ITEM = new jp.scid.bio.store.jooq.tables.CollectionItem();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<jp.scid.bio.store.jooq.tables.records.CollectionItemRecord> getRecordType() {
		return jp.scid.bio.store.jooq.tables.records.CollectionItemRecord.class;
	}

	/**
	 * 識別子
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.CollectionItemRecord, java.lang.Long> ID = createField("ID", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * フォルダ id
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT FK_COLLECTION_ITEM_FOLDER_ID
	 * FOREIGN KEY (FOLDER_ID, FOLDER_ID)
	 * REFERENCES PUBLIC.FOLDER (ID)
	 * </pre></code>
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.CollectionItemRecord, java.lang.Long> FOLDER_ID = createField("FOLDER_ID", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * 配列識別子
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT FK_COLLECTION_ITEM_GENETIC_SEQUENCE_ID
	 * FOREIGN KEY (GENETIC_SEQUENCE_ID)
	 * REFERENCES PUBLIC.GENETIC_SEQUENCE (ID)
	 * </pre></code>
	 */
	public final org.jooq.TableField<jp.scid.bio.store.jooq.tables.records.CollectionItemRecord, java.lang.Long> GENETIC_SEQUENCE_ID = createField("GENETIC_SEQUENCE_ID", org.jooq.impl.SQLDataType.BIGINT, this);

	public CollectionItem() {
		super("COLLECTION_ITEM", jp.scid.bio.store.jooq.Public.PUBLIC);
	}

	public CollectionItem(java.lang.String alias) {
		super(alias, jp.scid.bio.store.jooq.Public.PUBLIC, jp.scid.bio.store.jooq.tables.CollectionItem.COLLECTION_ITEM);
	}

	@Override
	public org.jooq.Identity<jp.scid.bio.store.jooq.tables.records.CollectionItemRecord, java.lang.Long> getIdentity() {
		return jp.scid.bio.store.jooq.Keys.IDENTITY_COLLECTION_ITEM;
	}

	@Override
	public org.jooq.UniqueKey<jp.scid.bio.store.jooq.tables.records.CollectionItemRecord> getMainKey() {
		return jp.scid.bio.store.jooq.Keys.CONSTRAINT_8;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<jp.scid.bio.store.jooq.tables.records.CollectionItemRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<jp.scid.bio.store.jooq.tables.records.CollectionItemRecord>>asList(jp.scid.bio.store.jooq.Keys.CONSTRAINT_8);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.ForeignKey<jp.scid.bio.store.jooq.tables.records.CollectionItemRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<jp.scid.bio.store.jooq.tables.records.CollectionItemRecord, ?>>asList(jp.scid.bio.store.jooq.Keys.FK_COLLECTION_ITEM_FOLDER_ID, jp.scid.bio.store.jooq.Keys.FK_COLLECTION_ITEM_GENETIC_SEQUENCE_ID);
	}

	@Override
	public jp.scid.bio.store.jooq.tables.CollectionItem as(java.lang.String alias) {
		return new jp.scid.bio.store.jooq.tables.CollectionItem(alias);
	}
}
