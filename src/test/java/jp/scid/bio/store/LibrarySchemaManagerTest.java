package jp.scid.bio.store;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;
import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LibrarySchemaManagerTest {
    static JdbcConnectionPool connectionPool;
    Factory create;
    Connection connection;
    LibrarySchemaManager manager;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ConnectionBuilder connBuilder = new ConnectionBuilder();
        connectionPool = connBuilder.build();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        connectionPool.dispose();
    }
    
    @Before
    public void setUp() throws Exception {
        create = new H2Factory(connection = connectionPool.getConnection());
        manager = new LibrarySchemaManager();
    }

    @After
    public void tearDown() throws Exception {
        manager.close();
        connection.close();
    }
    
    @Test
    public synchronized void setUpSchema() throws SQLException {
        manager = new LibrarySchemaManager();
        
        manager.open();
        
        assertFalse(manager.isSchemaReady());
        
        manager.setUpSchema();
        
        assertTrue(manager.isSchemaReady());
    }
}
