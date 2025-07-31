package org.databunker.options;

/**
 * Processing activity options class for creating processing activities
 */
public class ProcessingActivityOptions {
    private final String activity;
    private final String newactivity;
    private final String title;
    private final String script;
    private final String fulldesc;
    private final String applicableto;
    
    private ProcessingActivityOptions(Builder builder) {
        this.activity = builder.activity;
        this.newactivity = builder.newactivity;
        this.title = builder.title;
        this.script = builder.script;
        this.fulldesc = builder.fulldesc;
        this.applicableto = builder.applicableto;
    }
    
    /**
     * Activity identifier
     * @return The activity identifier
     */
    public String getActivity() {
        return activity;
    }
    
    /**
     * New activity identifier (for updates)
     * @return The new activity identifier
     */
    public String getNewactivity() {
        return newactivity;
    }
    
    /**
     * Title of the processing activity
     * @return The title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Script for the processing activity
     * @return The script
     */
    public String getScript() {
        return script;
    }
    
    /**
     * Full description of the processing activity
     * @return The full description
     */
    public String getFulldesc() {
        return fulldesc;
    }
    
    /**
     * Applicable to field
     * @return The applicable to value
     */
    public String getApplicableto() {
        return applicableto;
    }
    
    /**
     * Builder class for ProcessingActivityOptions
     */
    public static class Builder {
        private String activity;
        private String newactivity;
        private String title;
        private String script;
        private String fulldesc;
        private String applicableto;
        
        public Builder activity(String activity) {
            this.activity = activity;
            return this;
        }
        
        public Builder newactivity(String newactivity) {
            this.newactivity = newactivity;
            return this;
        }
        
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        
        public Builder script(String script) {
            this.script = script;
            return this;
        }
        
        public Builder fulldesc(String fulldesc) {
            this.fulldesc = fulldesc;
            return this;
        }
        
        public Builder applicableto(String applicableto) {
            this.applicableto = applicableto;
            return this;
        }
        
        public ProcessingActivityOptions build() {
            return new ProcessingActivityOptions(this);
        }
    }
    
    /**
     * Creates a new builder for ProcessingActivityOptions
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
} 