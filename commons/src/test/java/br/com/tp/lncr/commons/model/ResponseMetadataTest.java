package br.com.tp.lncr.commons.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseMetadataTest {
    @Test
    void testDefaultConstructor() {
        ResponseMetadata meta = new ResponseMetadata();
        assertNotNull(meta.getTraceId());
        assertNotNull(meta.getTimestamp());
        assertNull(meta.getMessage());
    }

    @Test
    void testConstructorWithAllFields() {
        ResponseMetadata meta = new ResponseMetadata("trace", "2025-07-18T12:00:00Z", "msg");
        assertEquals("trace", meta.getTraceId());
        assertEquals("2025-07-18T12:00:00Z", meta.getTimestamp());
        assertEquals("msg", meta.getMessage());
    }

    @Test
    void testConstructorWithoutMessage() {
        ResponseMetadata meta = new ResponseMetadata("trace2", "2025-07-18T13:00:00Z");
        assertEquals("trace2", meta.getTraceId());
        assertEquals("2025-07-18T13:00:00Z", meta.getTimestamp());
        assertNull(meta.getMessage());
    }

    @Test
    void testSettersAndGetters() {
        ResponseMetadata meta = new ResponseMetadata();
        meta.setTraceId("id");
        meta.setTimestamp("2025-07-18T14:00:00Z");
        meta.setMessage("mensagem");
        assertEquals("id", meta.getTraceId());
        assertEquals("2025-07-18T14:00:00Z", meta.getTimestamp());
        assertEquals("mensagem", meta.getMessage());
    }

    @Test
    void deveCriarResponseMetadataComTraceIdETimestamp() {
        String traceId = "trace-123";
        String timestamp = "2025-12-15T10:30:00Z";

        ResponseMetadata metadata = new ResponseMetadata(traceId, timestamp);

        assertEquals(traceId, metadata.getTraceId());
        assertEquals(timestamp, metadata.getTimestamp());
        assertNull(metadata.getMessage());
    }

    @Test
    void deveCriarResponseMetadataComTraceIdETimestampNulos() {
        ResponseMetadata metadata = new ResponseMetadata(null, null);

        assertNull(metadata.getTraceId());
        assertNull(metadata.getTimestamp());
        assertNull(metadata.getMessage());
    }

    @Test
    void deveCriarResponseMetadataComStringsVazias() {
        ResponseMetadata metadata = new ResponseMetadata("", "");

        assertEquals("", metadata.getTraceId());
        assertEquals("", metadata.getTimestamp());
        assertNull(metadata.getMessage());
    }

    @Test
    void deveDefinirTraceIdCorretamente() {
        ResponseMetadata metadata = new ResponseMetadata();
        String novoTraceId = "novo-trace-id-456";

        metadata.setTraceId(novoTraceId);

        assertEquals(novoTraceId, metadata.getTraceId());
    }

    @Test
    void devePermitirDefinirTraceIdComoNulo() {
        ResponseMetadata metadata = new ResponseMetadata("trace-inicial", "2025-12-15T10:00:00Z");

        metadata.setTraceId(null);

        assertNull(metadata.getTraceId());
    }

    @Test
    void deveSubstituirTraceIdExistente() {
        ResponseMetadata metadata = new ResponseMetadata("trace-antigo", "2025-12-15T10:00:00Z");
        String novoTraceId = "trace-novo";

        metadata.setTraceId(novoTraceId);

        assertEquals(novoTraceId, metadata.getTraceId());
    }

    @Test
    void deveDefinirTimestampCorretamente() {
        ResponseMetadata metadata = new ResponseMetadata();
        String novoTimestamp = "2025-12-15T15:45:30Z";

        metadata.setTimestamp(novoTimestamp);

        assertEquals(novoTimestamp, metadata.getTimestamp());
    }

    @Test
    void devePermitirDefinirTimestampComoNulo() {
        ResponseMetadata metadata = new ResponseMetadata("trace-123", "2025-12-15T10:00:00Z");

        metadata.setTimestamp(null);

        assertNull(metadata.getTimestamp());
    }

    @Test
    void deveSubstituirTimestampExistente() {
        ResponseMetadata metadata = new ResponseMetadata("trace-123", "2025-12-15T10:00:00Z");
        String novoTimestamp = "2025-12-15T16:00:00Z";

        metadata.setTimestamp(novoTimestamp);

        assertEquals(novoTimestamp, metadata.getTimestamp());
    }

    @Test
    void deveDefinirMessageCorretamente() {
        ResponseMetadata metadata = new ResponseMetadata();
        String mensagem = "Operação realizada com sucesso";

        metadata.setMessage(mensagem);

        assertEquals(mensagem, metadata.getMessage());
    }

    @Test
    void devePermitirDefinirMessageComoNulo() {
        ResponseMetadata metadata = new ResponseMetadata("trace-123", "2025-12-15T10:00:00Z", "mensagem inicial");

        metadata.setMessage(null);

        assertNull(metadata.getMessage());
    }

    @Test
    void deveSubstituirMessageExistente() {
        ResponseMetadata metadata = new ResponseMetadata("trace-123", "2025-12-15T10:00:00Z", "mensagem antiga");
        String novaMensagem = "mensagem atualizada";

        metadata.setMessage(novaMensagem);

        assertEquals(novaMensagem, metadata.getMessage());
    }

    @Test
    void deveDefinirMessageComStringVazia() {
        ResponseMetadata metadata = new ResponseMetadata();

        metadata.setMessage("");

        assertEquals("", metadata.getMessage());
    }

    @Test
    void deveManterIndependenciaEntreAtributos() {
        ResponseMetadata metadata = new ResponseMetadata("trace-original", "timestamp-original");

        metadata.setTraceId("novo-trace");
        metadata.setTimestamp("novo-timestamp");
        metadata.setMessage("nova-mensagem");

        assertEquals("novo-trace", metadata.getTraceId());
        assertEquals("novo-timestamp", metadata.getTimestamp());
        assertEquals("nova-mensagem", metadata.getMessage());
    }

    @Test
    void devePermitirMultiplasAtualizacoesDosMesmosAtributos() {
        ResponseMetadata metadata = new ResponseMetadata();

        metadata.setTraceId("trace-1");
        metadata.setTraceId("trace-2");
        metadata.setTraceId("trace-3");

        metadata.setTimestamp("timestamp-1");
        metadata.setTimestamp("timestamp-2");

        metadata.setMessage("msg-1");
        metadata.setMessage("msg-2");
        metadata.setMessage("msg-3");
        metadata.setMessage("msg-4");

        assertEquals("trace-3", metadata.getTraceId());
        assertEquals("timestamp-2", metadata.getTimestamp());
        assertEquals("msg-4", metadata.getMessage());
    }
}

