package jp.scid.bio.store;

import static jp.scid.bio.store.jooq.Tables.*;
import static org.junit.Assert.*;
import jp.scid.bio.store.jooq.Tables;
import jp.scid.bio.store.sequence.FileLibrary;

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
        
        library = new FileLibrary();
    }
    
    @After
    public void tearDown() throws Exception {
        create.getConnection().close();
    }

    
    @Test
    public void getAll() {
        int count = create.selectCount().from(Tables.GENETIC_SEQUENCE).fetchOne(0, Integer.class);
        assertEquals(1, count);
    }
}
