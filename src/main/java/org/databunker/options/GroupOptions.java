package org.databunker.options;

/**
 * Group options class for group creation and management
 */
public class GroupOptions {
    private final String groupname;
    private final String grouptype;
    private final String groupdesc;
    
    private GroupOptions(Builder builder) {
        this.groupname = builder.groupname;
        this.grouptype = builder.grouptype;
        this.groupdesc = builder.groupdesc;
    }
    
    /**
     * Group name
     * @return The group name
     */
    public String getGroupname() {
        return groupname;
    }
    
    /**
     * Group type
     * @return The group type
     */
    public String getGrouptype() {
        return grouptype;
    }
    
    /**
     * Group description
     * @return The group description
     */
    public String getGroupdesc() {
        return groupdesc;
    }
    
    /**
     * Builder class for GroupOptions
     */
    public static class Builder {
        private String groupname;
        private String grouptype;
        private String groupdesc;
        
        public Builder groupname(String groupname) {
            this.groupname = groupname;
            return this;
        }
        
        public Builder grouptype(String grouptype) {
            this.grouptype = grouptype;
            return this;
        }
        
        public Builder groupdesc(String groupdesc) {
            this.groupdesc = groupdesc;
            return this;
        }
        
        public GroupOptions build() {
            return new GroupOptions(this);
        }
    }
    
    /**
     * Creates a new builder for GroupOptions
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
} 