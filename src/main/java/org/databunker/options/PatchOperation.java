package org.databunker.options;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a patch operation for updating user data
 */
public class PatchOperation {
    @JsonProperty("op")
    private String op;

    @JsonProperty("path")
    private String path;

    @JsonProperty("value")
    private Object value;

    /**
     * Default constructor
     */
    public PatchOperation() {
    }

    /**
     * Constructor with all fields
     *
     * @param op    Operation type (e.g., 'add', 'replace', 'remove')
     * @param path  JSON path to the field to modify
     * @param value New value for the field
     */
    public PatchOperation(String op, String path, Object value) {
        this.op = op;
        this.path = path;
        this.value = value;
    }

    /**
     * Gets the operation type
     *
     * @return The operation type (e.g., 'add', 'replace', 'remove')
     */
    public String getOp() {
        return op;
    }

    /**
     * Sets the operation type
     *
     * @param op The operation type (e.g., 'add', 'replace', 'remove')
     */
    public void setOp(String op) {
        this.op = op;
    }

    /**
     * Gets the JSON path to the field to modify
     *
     * @return The JSON path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the JSON path to the field to modify
     *
     * @param path The JSON path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets the new value for the field
     *
     * @return The new value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the new value for the field
     *
     * @param value The new value
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
