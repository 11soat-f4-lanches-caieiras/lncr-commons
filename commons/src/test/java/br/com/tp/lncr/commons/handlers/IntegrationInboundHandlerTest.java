package br.com.tp.lncr.commons.handlers;

import br.com.tp.lncr.commons.integrations.IntegrationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationInboundHandlerTest {

    private final IntegrationInboundHandler handler = new IntegrationInboundHandler();

    @ParameterizedTest
    @CsvSource({
        "Erro de integração, 500, INTERNAL_SERVER_ERROR",
        "Recurso não encontrado, 404, NOT_FOUND",
        "Requisição inválida, 400, BAD_REQUEST"
    })
    void deveRetornarResponseEntityComCodigoHTTPCorreto(String mensagem, int codigo, HttpStatus statusEsperado) {
        IntegrationException exception = new IntegrationException(mensagem, codigo);
        ResponseEntity<Object> response = handler.handlerIntegrationException(exception);
        assertNotNull(response);
        assertEquals(statusEsperado, response.getStatusCode());
    }

    @Test
    void deveProcessarMensagemDeErroCorretamente() {
        String mensagemErro = "Falha na comunicação com serviço externo";
        IntegrationException exception = new IntegrationException(mensagemErro, 500);

        ResponseEntity<Object> response = handler.handlerIntegrationException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deveRetornarResponseEntityQuandoIntegrationExceptionComCodigo401() {
        IntegrationException exception = new IntegrationException("Não autorizado", 401);

        ResponseEntity<Object> response = handler.handlerIntegrationException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void deveRetornarResponseEntityQuandoIntegrationExceptionComCodigo403() {
        IntegrationException exception = new IntegrationException("Acesso negado", 403);

        ResponseEntity<Object> response = handler.handlerIntegrationException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void deveRetornarResponseEntityQuandoIntegrationExceptionComMensagemLonga() {
        String mensagemLonga = "Erro ao processar integração: timeout na conexão com o serviço externo após múltiplas tentativas de reconexão";
        IntegrationException exception = new IntegrationException(mensagemLonga, 500);

        ResponseEntity<Object> response = handler.handlerIntegrationException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deveRetornarResponseEntityQuandoIntegrationExceptionComMensagemVazia() {
        IntegrationException exception = new IntegrationException("", 500);

        ResponseEntity<Object> response = handler.handlerIntegrationException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deveRetornarBodyNaoNulo() {
        IntegrationException exception = new IntegrationException("Erro de teste", 500);

        ResponseEntity<Object> response = handler.handlerIntegrationException(exception);

        assertNotNull(response.getBody());
    }

    @Test
    void deveProcessarExcecaoComCaracteresEspeciais() {
        IntegrationException exception = new IntegrationException("Erro: áéíóú çñ @#$%", 500);

        ResponseEntity<Object> response = handler.handlerIntegrationException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
