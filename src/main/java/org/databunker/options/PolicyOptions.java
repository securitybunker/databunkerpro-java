package org.databunker.options;

/**
 * Policy options class for policy creation and management
 */
public class PolicyOptions {
    private final String policyname;
    private final String policydesc;
    private final Object policy; // Can be any type
    
    private PolicyOptions(Builder builder) {
        this.policyname = builder.policyname;
        this.policydesc = builder.policydesc;
        this.policy = builder.policy;
    }
    
    /**
     * Policy name
     * @return The policy name
     */
    public String getPolicyname() {
        return policyname;
    }
    
    /**
     * Policy description
     * @return The policy description
     */
    public String getPolicydesc() {
        return policydesc;
    }
    
    /**
     * Policy object (can be any type)
     * @return The policy object
     */
    public Object getPolicy() {
        return policy;
    }
    
    /**
     * Builder class for PolicyOptions
     */
    public static class Builder {
        private String policyname;
        private String policydesc;
        private Object policy;
        
        public Builder policyname(String policyname) {
            this.policyname = policyname;
            return this;
        }
        
        public Builder policydesc(String policydesc) {
            this.policydesc = policydesc;
            return this;
        }
        
        public Builder policy(Object policy) {
            this.policy = policy;
            return this;
        }
        
        public PolicyOptions build() {
            return new PolicyOptions(this);
        }
    }
    
    /**
     * Creates a new builder for PolicyOptions
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
} 