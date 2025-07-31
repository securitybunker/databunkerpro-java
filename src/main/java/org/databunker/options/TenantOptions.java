package org.databunker.options;

/**
 * Tenant options class for tenant creation and management
 */
public class TenantOptions {
    private final String tenantname;
    private final String tenantorg;
    private final String email;
    
    private TenantOptions(Builder builder) {
        this.tenantname = builder.tenantname;
        this.tenantorg = builder.tenantorg;
        this.email = builder.email;
    }
    
    /**
     * Tenant name
     * @return The tenant name
     */
    public String getTenantname() {
        return tenantname;
    }
    
    /**
     * Tenant organization
     * @return The tenant organization
     */
    public String getTenantorg() {
        return tenantorg;
    }
    
    /**
     * Email address
     * @return The email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Builder class for TenantOptions
     */
    public static class Builder {
        private String tenantname;
        private String tenantorg;
        private String email;
        
        public Builder tenantname(String tenantname) {
            this.tenantname = tenantname;
            return this;
        }
        
        public Builder tenantorg(String tenantorg) {
            this.tenantorg = tenantorg;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public TenantOptions build() {
            return new TenantOptions(this);
        }
    }
    
    /**
     * Creates a new builder for TenantOptions
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
} 