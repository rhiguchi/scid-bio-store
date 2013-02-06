package jp.scid.bio.store;

import org.h2.jdbcx.JdbcConnectionPool;

class ConnectionBuilder {
    String databaseNamespace = "~/scid-bio-lib/lib";
    
    private String databaseUser = "";
    private String databasePassword = "";
    
    public ConnectionBuilder() {
    }
    
    public JdbcConnectionPool build() {
        try {
            Class.forName("org.h2.Driver");
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("need h2 database driver", e);
        }
        JdbcConnectionPool connectionPool =
                JdbcConnectionPool.create(getDatabaseAddr(), databaseUser, databasePassword);
        return connectionPool;
    }
    
    public void databaseNamespace(String databaseNamespace) {
        this.databaseNamespace = databaseNamespace;
    }
    
    public void databaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }
    
    public void databasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
    
    private String getDatabaseAddr() {
        return "jdbc:h2:" + databaseNamespace;
    }
}
