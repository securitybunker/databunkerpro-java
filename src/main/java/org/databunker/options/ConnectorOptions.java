package org.databunker.options;

/**
 * Connector options class for connector configuration
 */
public class ConnectorOptions {
    private final Object connectorid; // Can be String or Number
    private final String connectorname;
    private final String connectortype;
    private final String apikey;
    private final String username;
    private final String connectordesc;
    private final String dbhost;
    private final Integer dbport;
    private final String dbname;
    private final String tablename;
    private final String status;
    
    private ConnectorOptions(Builder builder) {
        this.connectorid = builder.connectorid;
        this.connectorname = builder.connectorname;
        this.connectortype = builder.connectortype;
        this.apikey = builder.apikey;
        this.username = builder.username;
        this.connectordesc = builder.connectordesc;
        this.dbhost = builder.dbhost;
        this.dbport = builder.dbport;
        this.dbname = builder.dbname;
        this.tablename = builder.tablename;
        this.status = builder.status;
    }
    
    /**
     * Connector ID
     * @return The connector ID (String or Number)
     */
    public Object getConnectorid() {
        return connectorid;
    }
    
    /**
     * Connector name
     * @return The connector name
     */
    public String getConnectorname() {
        return connectorname;
    }
    
    /**
     * Connector type
     * @return The connector type
     */
    public String getConnectortype() {
        return connectortype;
    }
    
    /**
     * API key for the connector
     * @return The API key
     */
    public String getApikey() {
        return apikey;
    }
    
    /**
     * Username for the connector
     * @return The username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Connector description
     * @return The connector description
     */
    public String getConnectordesc() {
        return connectordesc;
    }
    
    /**
     * Database host
     * @return The database host
     */
    public String getDbhost() {
        return dbhost;
    }
    
    /**
     * Database port
     * @return The database port
     */
    public Integer getDbport() {
        return dbport;
    }
    
    /**
     * Database name
     * @return The database name
     */
    public String getDbname() {
        return dbname;
    }
    
    /**
     * Table name
     * @return The table name
     */
    public String getTablename() {
        return tablename;
    }
    
    /**
     * Status of the connector
     * @return The status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Builder class for ConnectorOptions
     */
    public static class Builder {
        private Object connectorid;
        private String connectorname;
        private String connectortype;
        private String apikey;
        private String username;
        private String connectordesc;
        private String dbhost;
        private Integer dbport;
        private String dbname;
        private String tablename;
        private String status;
        
        public Builder connectorid(String connectorid) {
            this.connectorid = connectorid;
            return this;
        }
        
        public Builder connectorid(Integer connectorid) {
            this.connectorid = connectorid;
            return this;
        }
        
        public Builder connectorname(String connectorname) {
            this.connectorname = connectorname;
            return this;
        }
        
        public Builder connectortype(String connectortype) {
            this.connectortype = connectortype;
            return this;
        }
        
        public Builder apikey(String apikey) {
            this.apikey = apikey;
            return this;
        }
        
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        
        public Builder connectordesc(String connectordesc) {
            this.connectordesc = connectordesc;
            return this;
        }
        
        public Builder dbhost(String dbhost) {
            this.dbhost = dbhost;
            return this;
        }
        
        public Builder dbport(Integer dbport) {
            this.dbport = dbport;
            return this;
        }
        
        public Builder dbname(String dbname) {
            this.dbname = dbname;
            return this;
        }
        
        public Builder tablename(String tablename) {
            this.tablename = tablename;
            return this;
        }
        
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        
        public ConnectorOptions build() {
            return new ConnectorOptions(this);
        }
    }
    
    /**
     * Creates a new builder for ConnectorOptions
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
} 