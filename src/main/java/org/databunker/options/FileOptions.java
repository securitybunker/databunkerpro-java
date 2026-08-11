package org.databunker.options;

import java.util.List;

/**
 * Options class for storing a file
 */
public class FileOptions {
    private final String mimetype;
    private final List<String> tags;
    private final String finaltime;
    private final String slidingtime;

    private FileOptions(Builder builder) {
        this.mimetype = builder.mimetype;
        this.tags = builder.tags;
        this.finaltime = builder.finaltime;
        this.slidingtime = builder.slidingtime;
    }

    /**
     * MIME type of the file
     * @return The MIME type, or null
     */
    public String getMimetype() {
        return mimetype;
    }

    /**
     * Tags carried by the file. Tags are lowercased and de-duplicated by the
     * server, must match ^[a-z0-9][a-z0-9._-]{0,49}$, and at most 16 are kept.
     * @return The tag list, or null
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Absolute expiration time for the file
     * @return The final time as a string (e.g., "100d", "1h")
     */
    public String getFinaltime() {
        return finaltime;
    }

    /**
     * Sliding time period for the file
     * @return The sliding time as a string (e.g., "30d", "1h")
     */
    public String getSlidingtime() {
        return slidingtime;
    }

    /**
     * Builder class for FileOptions
     */
    public static class Builder {
        private String mimetype;
        private List<String> tags;
        private String finaltime;
        private String slidingtime;

        public Builder mimetype(String mimetype) {
            this.mimetype = mimetype;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder finaltime(String finaltime) {
            this.finaltime = finaltime;
            return this;
        }

        public Builder slidingtime(String slidingtime) {
            this.slidingtime = slidingtime;
            return this;
        }

        public FileOptions build() {
            return new FileOptions(this);
        }
    }

    /**
     * Creates a new builder for FileOptions
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
}
