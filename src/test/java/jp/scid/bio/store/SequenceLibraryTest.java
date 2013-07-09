package jp.scid.bio.store;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.ParseException;
import java.util.List;

import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.sequence.GeneticSequence;

import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SequenceLibraryTest {
    Connection connection;
    SequenceLibrary library;
    Factory factory;
    
    @Before
    public void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:");
        factory = new H2Factory(connection);
        library = new SequenceLibrary(factory);
        
        library.setUpSchema();
    }
    
    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void importSequence() throws URISyntaxException, IOException, ParseException {
        File file = new File(getClass().getResource("NC_009347.gbk").toURI());
        
        library.importSequence(file);
        assertEquals(1, factory.selectCount().from(Tables.GENETIC_SEQUENCE).fetchOne(0));
    }

    @Test
    public void getGeneticSequences() {
        jp.scid.bio.store.jooq.tables.GeneticSequence table = Tables.GENETIC_SEQUENCE;
        factory.insertInto(table, table.NAME)
        .values("seq1")
        .values("seq2")
        .values("seq3")
        .execute();
        
        List<GeneticSequence> list = library.getGeneticSequences();
        assertEquals(3, list.size());
    }

    @Test
    public void deleteSequence() {
        jp.scid.bio.store.jooq.tables.GeneticSequence table = Tables.GENETIC_SEQUENCE;
        factory.insertInto(table, table.NAME)
        .values("seq1")
        .values("seq2")
        .values("seq3")
        .execute();
        
        List<GeneticSequence> list = library.getGeneticSequences();
        GeneticSequence sequence = list.get(0);
        
        library.deleteSequence(sequence, false);
        
        assertEquals(2, library.getGeneticSequences().size());
    }
}
