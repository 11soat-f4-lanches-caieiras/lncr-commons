package br.com.tp.lncr.commons.model;

import br.com.tp.lncr.core.model.ResponseMetadata;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseModelTest {

    @Test
    void deveCriarResponseModelComMetadataEConteudo() {
        ResponseMetadata metadata = new ResponseMetadata("txn-123", "2023-07-21T10:00:00Z", "Sucesso");
        String content = "Teste de conteúdo";

        ResponseModel<String> response = new ResponseModel<>(metadata, content);

        assertEquals(metadata, response.get_response());
        assertEquals(content, response.get_content());
        assertEquals("txn-123", response.get_response().getTraceId());
        assertEquals("2023-07-21T10:00:00Z", response.get_response().getTimestamp());
        assertEquals("Sucesso", response.get_response().getMessage());
    }

    @Test
    void deveCriarResponseModelApenasComConteudo() {
        String content = "Teste de conteúdo";

        ResponseModel<String> response = new ResponseModel<>(content);

        assertNotNull(response.get_response());
        assertEquals(content, response.get_content());
        assertNotNull(response.get_response().getTraceId());
        assertNotNull(response.get_response().getTimestamp());
        assertNull(response.get_response().getMessage());
    }

    @Test
    void deveCriarResponseModelComConteudoNulo() {
        ResponseModel<String> response = new ResponseModel<>(null);

        assertNotNull(response.get_response());
        assertNull(response.get_content());
    }

    @Test
    void deveManterTipoGenericoDoConteudo() {
        Integer numeroInteiro = 42;
        ResponseModel<Integer> response = new ResponseModel<>(numeroInteiro);

        assertNotNull(response.get_response());
        assertEquals(42, response.get_content());
        assertInstanceOf(Integer.class, response.get_content());
    }

    @Test
    void devePreservarMetadataOriginal() {
        ResponseMetadata metadata = new ResponseMetadata("txn-456", "2023-07-21T11:00:00Z", "Processado");
        String content = "Dados processados";

        ResponseModel<String> response = new ResponseModel<>(metadata, content);

        assertSame(metadata, response.get_response());
        assertEquals("txn-456", response.get_response().getTraceId());
        assertEquals("2023-07-21T11:00:00Z", response.get_response().getTimestamp());
        assertEquals("Processado", response.get_response().getMessage());
    }

    @Test
    void devePermitirConteudoComplexo() {
        class TestObject {
            private final String name;
            private final int value;

            public TestObject(String name, int value) {
                this.name = name;
                this.value = value;
            }

            public String getName() { return name; }
            public int getValue() { return value; }
        }

        TestObject objeto = new TestObject("teste", 100);
        ResponseModel<TestObject> response = new ResponseModel<>(objeto);

        assertNotNull(response.get_response());
        assertEquals(objeto, response.get_content());
        assertEquals("teste", response.get_content().getName());
        assertEquals(100, response.get_content().getValue());
    }

    @Test
    void deveGerarMetadataAutomaticaComTimestampValido() {
        String content = "Teste timestamp";

        ResponseModel<String> response = new ResponseModel<>(content);

        assertNotNull(response.get_response().getTimestamp());
        assertFalse(response.get_response().getTimestamp().isEmpty());
    }

    @Test
    void deveGerarTransactionIdUnicoAutomaticamente() {
        ResponseModel<String> response1 = new ResponseModel<>("Conteúdo 1");
        ResponseModel<String> response2 = new ResponseModel<>("Conteúdo 2");

        assertNotEquals(response1.get_response().getTraceId(),
                       response2.get_response().getTraceId());
    }
}
