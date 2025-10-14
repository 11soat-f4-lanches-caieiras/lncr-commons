package br.com.tp.lncr.commons.utils;

import br.com.tp.lncr.commons.model.ResponseListModel;
import br.com.tp.lncr.commons.model.ResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseEntityModelUtilTest {

    @Test
    void deveRetornarResponseEntityComStatusEHeadersPersonalizados() {
        String body = "Teste";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "CustomValue");

        ResponseEntity<ResponseModel<String>> response = ResponseEntityModelUtil.response(body, HttpStatus.CREATED, headers);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("CustomValue", response.getHeaders().getFirst("Custom-Header"));
        assertNotNull(response.getBody());
        assertEquals("Teste", response.getBody().get_content());
        assertNotNull(response.getBody().get_response());
    }

    @Test
    void deveRetornarResponseEntityComStatusPersonalizadoSemHeaders() {
        Integer body = 123;

        ResponseEntity<ResponseModel<Integer>> response = ResponseEntityModelUtil.response(body, HttpStatus.ACCEPTED, null);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(123, response.getBody().get_content());
    }

    @Test
    void deveRetornarOKComCorpoValido() {
        String body = "Sucesso";

        ResponseEntity<ResponseModel<String>> response = ResponseEntityModelUtil.OK(body);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sucesso", response.getBody().get_content());
        assertNotNull(response.getBody().get_response());
    }

    @Test
    void deveRetornarOKComCorpoNulo() {
        ResponseEntity<ResponseModel<String>> response = ResponseEntityModelUtil.OK(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().get_content());
    }

    @Test
    void deveRetornarAcceptedComCorpoValido() {
        String body = "Aceito";

        ResponseEntity<ResponseModel<String>> response = ResponseEntityModelUtil.Accepted(body);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Aceito", response.getBody().get_content());
        assertNotNull(response.getBody().get_response());
    }

    @Test
    void deveRetornarCreatedComLocationHeader() {
        String body = "Criado";
        String location = "/api/resource/123";

        ResponseEntity<ResponseModel<String>> response = ResponseEntityModelUtil.created(body, location);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(location, response.getHeaders().getFirst("Location"));
        assertNotNull(response.getBody());
        assertEquals("Criado", response.getBody().get_content());
    }

    @Test
    void deveRetornarCreatedComLocationNula() {
        String body = "Criado sem location";

        ResponseEntity<ResponseModel<String>> response = ResponseEntityModelUtil.created(body, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getHeaders().getFirst("Location"));
        assertNotNull(response.getBody());
        assertEquals("Criado sem location", response.getBody().get_content());
    }

    @Test
    void deveRetornarListOKComListaValida() {
        List<String> body = Arrays.asList("Item1", "Item2", "Item3");

        ResponseEntity<ResponseListModel<String>> response = ResponseEntityModelUtil.listOK(body);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().get_content().size());
        assertEquals("Item1", response.getBody().get_content().get(0));
        assertEquals("Item2", response.getBody().get_content().get(1));
        assertEquals("Item3", response.getBody().get_content().get(2));
        assertNotNull(response.getBody().get_response());
    }

    @Test
    void deveRetornarListOKComListaVazia() {
        List<String> body = Collections.emptyList();

        ResponseEntity<ResponseListModel<String>> response = ResponseEntityModelUtil.listOK(body);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get_content().isEmpty());
        assertNotNull(response.getBody().get_response());
    }

    @Test
    void deveRetornarListOKComListaNula() {
        ResponseEntity<ResponseListModel<String>> response = ResponseEntityModelUtil.listOK(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().get_content());
    }

    @Test
    void devePreservarTipoGenericoEmResponseModel() {
        Integer numero = 42;

        ResponseEntity<ResponseModel<Integer>> response = ResponseEntityModelUtil.OK(numero);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Integer.class, response.getBody().get_content());
        assertEquals(42, response.getBody().get_content());
    }

    @Test
    void devePreservarTipoGenericoEmResponseListModel() {
        List<Integer> numeros = Arrays.asList(1, 2, 3);

        ResponseEntity<ResponseListModel<Integer>> response = ResponseEntityModelUtil.listOK(numeros);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Integer.class, response.getBody().get_content().get(0));
        assertEquals(3, response.getBody().get_content().size());
    }
}
