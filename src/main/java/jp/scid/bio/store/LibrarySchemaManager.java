package jp.scid.bio.store;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.h2.jdbcx.JdbcConnectionPool;
import org.jooq.Field;
import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;

public class LibrarySchemaManager implements Closeable {
    private final Field<String> schemaField = Factory.field("table_name", String.class);
    private final ConnectionBuilder connectionBuilder;
    private JdbcConnectionPool connectionPool = null;
    
    Connection connection = null;
    
    public LibrarySchemaManager() {
        this.connectionBuilder = new ConnectionBuilder();
    }
    
    public void setDatabaseNamespace(String databaseNamespace) {
        connectionBuilder.databaseNamespace(databaseNamespace);
    }

    public void setDatabaseUser(String databaseUser) {
        connectionBuilder.databaseUser(databaseUser);
    }

    public void setDatabasePassword(String databasePassword) {
        connectionBuilder.databasePassword(databasePassword);
    }

    public void open() throws SQLException {
        if (connectionPool != null) {
            throw new IllegalStateException("already open");
        }
        connectionPool = connectionBuilder.build();
        connectionPool.setMaxConnections(1);
        
        connection = connectionPool.getConnection();
    }
    
    @Override
    public void close() {
        if (connectionPool == null) {
            return;
        }
        try {
            connectionPool.dispose();
        }
        finally {
            connectionPool = null;
        }
    }
    
    public boolean isSchemaReady() {
        Factory factory = new H2Factory(getConnection());
        List<String> names = getTableNames(factory);
        return !names.isEmpty();
    }

    private Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("database has not been opened yet");
        }
        return connection;
    }

    public void setUpSchema() {
        Factory factory = new H2Factory(getConnection());
        
        // build schema tables
        String sql;
        try {
            sql = getSchemaSql();
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }

        factory.execute(sql);
    }
    
    public SequenceLibrary createSequenceLibrary() {
        Factory factory = new H2Factory(getConnection());
        SequenceLibrary library = new SequenceLibrary(factory);
        return library;
    }

    protected String getSchemaSql() throws IOException {
        InputStream resource = LibrarySchemaManager.class.getResourceAsStream("sql/schema.sql");
        Reader reader = new InputStreamReader(resource);
        
        StringBuilder sql = new StringBuilder();
        CharBuffer buf = CharBuffer.allocate(8196);
        try {
            while (reader.read(buf) >= 0) {
                sql.append(buf.flip());
                buf.clear();
            }
        }
        finally {
            reader.close();
        }
        
        return sql.toString();
    }

    private List<String> getTableNames(Factory factory) {
        List<String> tableNames = factory
                .select(schemaField).from("information_schema.tables")
                .where("table_schema = ?", "PUBLIC").fetch(schemaField);
        return tableNames;
    }
}
