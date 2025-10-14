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
        assertEquals("Erro de validação", metadata.get_message());
        assertNotNull(metadata.get_traceId());
        assertNotNull(metadata.get_timestamp());
    }

    @Test
    void deveRetornarUnauthorizedParaCodigo401() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Não autorizado", 401, null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertNotNull(metadata);
        assertEquals("Não autorizado", metadata.get_message());
    }

    @Test
    void deveRetornarForbiddenParaCodigo403() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Acesso negado", 403, null);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertNotNull(metadata);
        assertEquals("Acesso negado", metadata.get_message());
    }

    @Test
    void deveRetornarNotFoundParaCodigo404() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Recurso não encontrado", 404, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertEquals("Recurso não encontrado", metadata.get_message());
    }

    @Test
    void deveRetornarConflictParaCodigo409() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Conflito de dados", 409, null);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertEquals("Conflito de dados", metadata.get_message());
    }

    @Test
    void deveRetornarInternalServerErrorParaCodigo500() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Erro interno", 500, null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertEquals("Erro interno", metadata.get_message());
    }

    @Test
    void deveRetornarInternalServerErrorParaCodigoDesconhecido() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Erro desconhecido", 999, null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertEquals("Erro desconhecido", metadata.get_message());
    }

    @Test
    void deveProcessarExcecaoComThrowable() {
        RuntimeException exception = new RuntimeException("Teste de Erro");

        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Mensagem de erro", 500, exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();
        assertEquals("Mensagem de erro", metadata.get_message());
    }

    @Test
    void deveGerarTransactionIdUnico() {
        ResponseEntity<Object> response1 = ExceptionHandlerUtil.handleException("Erro 1", 400, null);
        ResponseEntity<Object> response2 = ExceptionHandlerUtil.handleException("Erro 2", 400, null);

        ResponseMetadata metadata1 = (ResponseMetadata) response1.getBody();
        ResponseMetadata metadata2 = (ResponseMetadata) response2.getBody();

        assertNotEquals(metadata1.get_traceId(), metadata2.get_traceId());
    }

    @Test
    void deveGerarTimestampValido() {
        ResponseEntity<Object> response = ExceptionHandlerUtil.handleException("Teste timestamp", 400, null);
        ResponseMetadata metadata = (ResponseMetadata) response.getBody();

        assertNotNull(metadata.get_timestamp());
        assertFalse(metadata.get_timestamp().isEmpty());
    }
}
