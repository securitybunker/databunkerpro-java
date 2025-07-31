package org.databunker.options;

/**
 * Token options class for token creation and management
 */
public class TokenOptions {
    private final Boolean unique;
    private final String slidingtime;
    private final String finaltime;
    
    private TokenOptions(Builder builder) {
        this.unique = builder.unique;
        this.slidingtime = builder.slidingtime;
        this.finaltime = builder.finaltime;
    }
    
    /**
     * Whether the token should be unique
     * @return The unique flag
     */
    public Boolean getUnique() {
        return unique;
    }
    
    /**
     * Sliding time period for the token
     * @return The sliding time as a string (e.g., "30d", "1h")
     */
    public String getSlidingtime() {
        return slidingtime;
    }
    
    /**
     * Absolute expiration time for the token
     * @return The final time as a string (e.g., "100d", "1h")
     */
    public String getFinaltime() {
        return finaltime;
    }
    
    /**
     * Builder class for TokenOptions
     */
    public static class Builder {
        private Boolean unique;
        private String slidingtime;
        private String finaltime;
        
        public Builder unique(Boolean unique) {
            this.unique = unique;
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
        
        public TokenOptions build() {
            return new TokenOptions(this);
        }
    }
    
    /**
     * Creates a new builder for TokenOptions
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
} 