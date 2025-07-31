package org.databunker.options;

/**
 * Role options class for role creation and management
 */
public class RoleOptions {
    private final String rolename;
    private final String roledesc;
    
    private RoleOptions(Builder builder) {
        this.rolename = builder.rolename;
        this.roledesc = builder.roledesc;
    }
    
    /**
     * Role name
     * @return The role name
     */
    public String getRolename() {
        return rolename;
    }
    
    /**
     * Role description
     * @return The role description
     */
    public String getRoledesc() {
        return roledesc;
    }
    
    /**
     * Builder class for RoleOptions
     */
    public static class Builder {
        private String rolename;
        private String roledesc;
        
        public Builder rolename(String rolename) {
            this.rolename = rolename;
            return this;
        }
        
        public Builder roledesc(String roledesc) {
            this.roledesc = roledesc;
            return this;
        }
        
        public RoleOptions build() {
            return new RoleOptions(this);
        }
    }
    
    /**
     * Creates a new builder for RoleOptions
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
} 