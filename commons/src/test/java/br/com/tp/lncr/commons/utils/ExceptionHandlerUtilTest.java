package br.com.tp.lncr.commons.utils;

import br.com.tp.lncr.core.model.ResponseMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerUtilTest {

    @Test
    void deveRetornarBadRequestParaCodigo400() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Erro de validação", 400, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ResponseMetadata.class, response.getBody());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertEquals("Erro de validação", metadata.getMessage());
        assertNotNull(metadata.getTraceId());
        assertNotNull(metadata.getTimestamp());
    }

    @Test
    void deveRetornarUnauthorizedParaCodigo401() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Não autorizado", 401, null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertNotNull(metadata);
        assertEquals("Não autorizado", metadata.getMessage());
    }

    @Test
    void deveRetornarForbiddenParaCodigo403() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Acesso negado", 403, null);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertNotNull(metadata);
        assertEquals("Acesso negado", metadata.getMessage());
    }

    @Test
    void deveRetornarNotFoundParaCodigo404() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Recurso não encontrado", 404, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertNotNull(metadata);
        assertEquals("Recurso não encontrado", metadata.getMessage());
    }

    @Test
    void deveRetornarConflictParaCodigo409() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Conflito de dados", 409, null);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertNotNull(metadata);
        assertEquals("Conflito de dados", metadata.getMessage());
    }

    @Test
    void deveRetornarInternalServerErrorParaCodigo500() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Erro interno", 500, null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertNotNull(metadata);
        assertEquals("Erro interno", metadata.getMessage());
    }

    @Test
    void deveRetornarInternalServerErrorParaCodigoDesconhecido() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Erro desconhecido", 999, null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertNotNull(metadata);
        assertEquals("Erro desconhecido", metadata.getMessage());
    }

    @Test
    void deveProcessarExcecaoComThrowable() {
        RuntimeException exception = new RuntimeException("Teste de Erro");

        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Mensagem de erro", 500, exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertNotNull(metadata);
        assertEquals("Mensagem de erro", metadata.getMessage());
    }

    @Test
    void deveGerarTransactionIdUnico() {
        ResponseEntity<Object> response1 = ExceptionHandlerUtil.handleException("Erro 1", 400, null);
        ResponseEntity<Object> response2 = ExceptionHandlerUtil.handleException("Erro 2", 400, null);

        ResponseMetadata metadata1 = (ResponseMetadata) response1.getBody();
        ResponseMetadata metadata2 = (ResponseMetadata) response2.getBody();

        assertNotNull(metadata1);
        assertNotNull(metadata2);
        assertNotEquals(metadata1.getTraceId(), metadata2.getTraceId());
    }

    @Test
    void deveGerarTimestampValido() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Teste timestamp", 400, null);
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();

        assertNotNull(metadata);
        assertNotNull(metadata.getTimestamp());
        assertFalse(metadata.getTimestamp().isEmpty());
    }
}
