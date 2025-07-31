package org.databunker.options;

/**
 * Basic options class for time-based operations
 */
public class BasicOptions {
    private final String finaltime;
    private final String slidingtime;
    
    private BasicOptions(Builder builder) {
        this.finaltime = builder.finaltime;
        this.slidingtime = builder.slidingtime;
    }
    
    /**
     * Absolute expiration time for the operation
     * @return The final time as a string (e.g., "100d", "1h")
     */
    public String getFinaltime() {
        return finaltime;
    }
    
    /**
     * Sliding time period for the operation
     * @return The sliding time as a string (e.g., "30d", "1h")
     */
    public String getSlidingtime() {
        return slidingtime;
    }
    
    /**
     * Builder class for BasicOptions
     */
    public static class Builder {
        private String finaltime;
        private String slidingtime;
        
        public Builder finaltime(String finaltime) {
            this.finaltime = finaltime;
            return this;
        }
        
        public Builder slidingtime(String slidingtime) {
            this.slidingtime = slidingtime;
            return this;
        }
        
        public BasicOptions build() {
            return new BasicOptions(this);
        }
    }
    
    /**
     * Creates a new builder for BasicOptions
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
} 