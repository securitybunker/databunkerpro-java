package org.databunker.options;

/**
 * Agreement accept options class for accepting agreements
 */
public class AgreementAcceptOptions {
    private final String agreementmethod;
    private final String referencecode;
    private final String starttime;
    private final String finaltime;
    private final String status;
    private final String lastmodifiedby;
    
    private AgreementAcceptOptions(Builder builder) {
        this.agreementmethod = builder.agreementmethod;
        this.referencecode = builder.referencecode;
        this.starttime = builder.starttime;
        this.finaltime = builder.finaltime;
        this.status = builder.status;
        this.lastmodifiedby = builder.lastmodifiedby;
    }
    
    /**
     * Method of agreement (e.g., 'web-form', 'checkbox', 'signature')
     * @return The agreement method
     */
    public String getAgreementmethod() {
        return agreementmethod;
    }
    
    /**
     * External reference code or identifier for this acceptance
     * @return The reference code
     */
    public String getReferencecode() {
        return referencecode;
    }
    
    /**
     * Start time of the agreement validity (ISO 8601 format)
     * @return The start time
     */
    public String getStarttime() {
        return starttime;
    }
    
    /**
     * End time of the agreement validity (ISO 8601 format)
     * @return The final time
     */
    public String getFinaltime() {
        return finaltime;
    }
    
    /**
     * Status of the agreement (e.g., 'pending', 'active', 'expired')
     * @return The status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Identifier of the person/system that last modified this agreement
     * @return The last modified by identifier
     */
    public String getLastmodifiedby() {
        return lastmodifiedby;
    }
    
    /**
     * Builder class for AgreementAcceptOptions
     */
    public static class Builder {
        private String agreementmethod;
        private String referencecode;
        private String starttime;
        private String finaltime;
        private String status;
        private String lastmodifiedby;
        
        public Builder agreementmethod(String agreementmethod) {
            this.agreementmethod = agreementmethod;
            return this;
        }
        
        public Builder referencecode(String referencecode) {
            this.referencecode = referencecode;
            return this;
        }
        
        public Builder starttime(String starttime) {
            this.starttime = starttime;
            return this;
        }
        
        public Builder finaltime(String finaltime) {
            this.finaltime = finaltime;
            return this;
        }
        
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        
        public Builder lastmodifiedby(String lastmodifiedby) {
            this.lastmodifiedby = lastmodifiedby;
            return this;
        }
        
        public AgreementAcceptOptions build() {
            return new AgreementAcceptOptions(this);
        }
    }
    
    /**
     * Creates a new builder for AgreementAcceptOptions
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
} 