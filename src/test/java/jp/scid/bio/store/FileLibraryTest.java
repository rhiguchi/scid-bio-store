package jp.scid.bio.store;

import static jp.scid.bio.store.jooq.Tables.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;

import jp.scid.bio.store.FileLibrary.UnknownSequenceFormatException;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.jooq.tables.records.GeneticSequenceRecord;

import org.h2.jdbcx.JdbcConnectionPool;
import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileLibraryTest {
    static JdbcConnectionPool connectionPool;
    Factory create;
    FileLibrary library;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ConnectionBuilder connBuilder = new ConnectionBuilder();
        connBuilder.databaseNamespace("tcp://localhost/~/GenomeMuseum/lib");
        connBuilder.databaseUser("genomemuseum");
        connectionPool = connBuilder.build();
        
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        connectionPool.dispose();
    }
    
    @Before
    public void setUp() throws Exception {
        create = new H2Factory(connectionPool.getConnection());
        create.insertInto(Tables.GENETIC_SEQUENCE).set(GENETIC_SEQUENCE.ID, (Long) null).execute();
        
        library = new FileLibrary(create);
    }
    
    @After
    public void tearDown() throws Exception {
        create.getConnection().close();
    }

    @Test
    public void add() throws URISyntaxException, UnknownSequenceFormatException, IOException, ParseException {
        URL resource = getClass().getResource("NC_009347.gbk");
        File file = new File(resource.toURI());
        
        GeneticSequenceRecord record = library.add(file);
        
        assertNotNull(record);
        assertEquals("NC_009347", record.getName());
        assertEquals(2101, record.getLength().intValue());
    }

    
    @Test
    public void getAll() {
        int count = create.selectCount().from(Tables.GENETIC_SEQUENCE).fetchOne(0, Integer.class);
        assertEquals(1, count);
    }
}
