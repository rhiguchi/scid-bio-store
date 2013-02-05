/**
 * This class is generated by jOOQ
 */
package jp.scid.bio.store.jooq;

/**
 * This class is generated by jOOQ.
 *
 * A class modelling foreign key relationships between tables of the <code>PUBLIC</code> 
 * schema
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class Keys {

	// IDENTITY definitions
	public static final org.jooq.Identity<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.Long> IDENTITY_GENETIC_SEQUENCE = Identities0.IDENTITY_GENETIC_SEQUENCE;

	// UNIQUE and PRIMARY KEY definitions
	public static final org.jooq.UniqueKey<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord> CONSTRAINT_5 = UniqueKeys0.CONSTRAINT_5;

	// FOREIGN KEY definitions

	/**
	 * No instances
	 */
	private Keys() {}

	@SuppressWarnings("hiding")
	private static class Identities0 extends org.jooq.impl.AbstractKeys {
		public static org.jooq.Identity<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord, java.lang.Long> IDENTITY_GENETIC_SEQUENCE = createIdentity(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE, jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.ID);
	}

	@SuppressWarnings({"hiding", "unchecked"})
	private static class UniqueKeys0 extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.UniqueKey<jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord> CONSTRAINT_5 = createUniqueKey(jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE, jp.scid.bio.store.jooq.tables.GeneticSequence.GENETIC_SEQUENCE.ID);
	}
}
