package br.com.tp.lncr.commons.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMetadata {
    private String traceId;
    private String timestamp;
    private String message;

    public ResponseMetadata() {
        this.traceId = UUID.randomUUID().toString();
        this.timestamp = Instant.now().toString();
        this.message = null;
    }

    public ResponseMetadata(String traceid, String timestamp, String message) {
        this.traceId = traceid;
        this.timestamp = timestamp;
        this.message = message;
    }

    public ResponseMetadata(String traceId, String timestamp) {
        this.traceId = traceId;
        this.timestamp = timestamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
