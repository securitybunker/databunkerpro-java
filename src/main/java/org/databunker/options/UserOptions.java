package org.databunker.options;

/**
 * User options class for user creation and management
 */
public class UserOptions {
    private final Object groupname; // Can be String or Number
    private final Integer groupid;
    private final Object rolename; // Can be String or Number
    private final Integer roleid;
    private final String slidingtime;
    private final String finaltime;
    
    private UserOptions(Builder builder) {
        this.groupname = builder.groupname;
        this.groupid = builder.groupid;
        this.rolename = builder.rolename;
        this.roleid = builder.roleid;
        this.slidingtime = builder.slidingtime;
        this.finaltime = builder.finaltime;
    }
    
    /**
     * Group name or ID for the user
     * @return The group name (String) or group ID (Number)
     */
    public Object getGroupname() {
        return groupname;
    }
    
    /**
     * Group ID for the user
     * @return The group ID
     */
    public Integer getGroupid() {
        return groupid;
    }
    
    /**
     * Role name or ID for the user
     * @return The role name (String) or role ID (Number)
     */
    public Object getRolename() {
        return rolename;
    }
    
    /**
     * Role ID for the user
     * @return The role ID
     */
    public Integer getRoleid() {
        return roleid;
    }
    
    /**
     * Sliding time period for the user
     * @return The sliding time as a string (e.g., "30d", "1h")
     */
    public String getSlidingtime() {
        return slidingtime;
    }
    
    /**
     * Absolute expiration time for the user
     * @return The final time as a string (e.g., "100d", "1h")
     */
    public String getFinaltime() {
        return finaltime;
    }
    
    /**
     * Builder class for UserOptions
     */
    public static class Builder {
        private Object groupname;
        private Integer groupid;
        private Object rolename;
        private Integer roleid;
        private String slidingtime;
        private String finaltime;
        
        public Builder groupname(String groupname) {
            this.groupname = groupname;
            return this;
        }
        
        public Builder groupname(Integer groupname) {
            this.groupname = groupname;
            return this;
        }
        
        public Builder groupid(Integer groupid) {
            this.groupid = groupid;
            return this;
        }
        
        public Builder rolename(String rolename) {
            this.rolename = rolename;
            return this;
        }
        
        public Builder rolename(Integer rolename) {
            this.rolename = rolename;
            return this;
        }
        
        public Builder roleid(Integer roleid) {
            this.roleid = roleid;
            return this;
        }
        
        public Builder slidingtime(String slidingtime) {
            this.slidingtime = slidingtime;
            return this;
        }
        
        public Builder finaltime(String finaltime) {
            this.finaltime = finaltime;
            return this;
        }
        
        public UserOptions build() {
            return new UserOptions(this);
        }
    }
    
    /**
     * Creates a new builder for UserOptions
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
} 