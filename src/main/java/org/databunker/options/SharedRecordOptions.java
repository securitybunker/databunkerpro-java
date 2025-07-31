package org.databunker.options;

/**
 * Shared record options class for creating shared records
 */
public class SharedRecordOptions {
    private final String fields; // A string containing names of fields to share separated by commas
    private final String partner; // It is used as a reference to partner name. It is not enforced.
    private final String appname; // If defined, shows fields from the user app record instead of user profile
    private final String finaltime; // Expiration time for the shared record
    
    private SharedRecordOptions(Builder builder) {
        this.fields = builder.fields;
        this.partner = builder.partner;
        this.appname = builder.appname;
        this.finaltime = builder.finaltime;
    }
    
    /**
     * A string containing names of fields to share separated by commas
     * @return The fields string
     */
    public String getFields() {
        return fields;
    }
    
    /**
     * It is used as a reference to partner name. It is not enforced.
     * @return The partner name
     */
    public String getPartner() {
        return partner;
    }
    
    /**
     * If defined, shows fields from the user app record instead of user profile
     * @return The app name
     */
    public String getAppname() {
        return appname;
    }
    
    /**
     * Expiration time for the shared record
     * @return The final time as a string (e.g., "100d", "1h")
     */
    public String getFinaltime() {
        return finaltime;
    }
    
    /**
     * Builder class for SharedRecordOptions
     */
    public static class Builder {
        private String fields;
        private String partner;
        private String appname;
        private String finaltime;
        
        public Builder fields(String fields) {
            this.fields = fields;
            return this;
        }
        
        public Builder partner(String partner) {
            this.partner = partner;
            return this;
        }
        
        public Builder appname(String appname) {
            this.appname = appname;
            return this;
        }
        
        public Builder finaltime(String finaltime) {
            this.finaltime = finaltime;
            return this;
        }
        
        public SharedRecordOptions build() {
            return new SharedRecordOptions(this);
        }
    }
    
    /**
     * Creates a new builder for SharedRecordOptions
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
} 