package org.databunker.options;

/**
 * Legal basis options class for creating legal basis records
 */
public class LegalBasisOptions {
    private final String brief;
    private final String status;
    private final String module;
    private final String fulldesc;
    private final String shortdesc;
    private final String basistype;
    private final String requiredmsg;
    private final Boolean requiredflag;
    
    private LegalBasisOptions(Builder builder) {
        this.brief = builder.brief;
        this.status = builder.status;
        this.module = builder.module;
        this.fulldesc = builder.fulldesc;
        this.shortdesc = builder.shortdesc;
        this.basistype = builder.basistype;
        this.requiredmsg = builder.requiredmsg;
        this.requiredflag = builder.requiredflag;
    }
    
    /**
     * Brief identifier for the legal basis
     * @return The brief identifier
     */
    public String getBrief() {
        return brief;
    }
    
    /**
     * Status of the legal basis
     * @return The status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Module associated with the legal basis
     * @return The module
     */
    public String getModule() {
        return module;
    }
    
    /**
     * Full description of the legal basis
     * @return The full description
     */
    public String getFulldesc() {
        return fulldesc;
    }
    
    /**
     * Short description of the legal basis
     * @return The short description
     */
    public String getShortdesc() {
        return shortdesc;
    }
    
    /**
     * Type of legal basis
     * @return The basis type
     */
    public String getBasistype() {
        return basistype;
    }
    
    /**
     * Required message for the legal basis
     * @return The required message
     */
    public String getRequiredmsg() {
        return requiredmsg;
    }
    
    /**
     * Required flag for the legal basis
     * @return The required flag
     */
    public Boolean getRequiredflag() {
        return requiredflag;
    }
    
    /**
     * Builder class for LegalBasisOptions
     */
    public static class Builder {
        private String brief;
        private String status;
        private String module;
        private String fulldesc;
        private String shortdesc;
        private String basistype;
        private String requiredmsg;
        private Boolean requiredflag;
        
        public Builder brief(String brief) {
            this.brief = brief;
            return this;
        }
        
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        
        public Builder module(String module) {
            this.module = module;
            return this;
        }
        
        public Builder fulldesc(String fulldesc) {
            this.fulldesc = fulldesc;
            return this;
        }
        
        public Builder shortdesc(String shortdesc) {
            this.shortdesc = shortdesc;
            return this;
        }
        
        public Builder basistype(String basistype) {
            this.basistype = basistype;
            return this;
        }
        
        public Builder requiredmsg(String requiredmsg) {
            this.requiredmsg = requiredmsg;
            return this;
        }
        
        public Builder requiredflag(Boolean requiredflag) {
            this.requiredflag = requiredflag;
            return this;
        }
        
        public LegalBasisOptions build() {
            return new LegalBasisOptions(this);
        }
    }
    
    /**
     * Creates a new builder for LegalBasisOptions
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
} 