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

        assertEquals(metadata, response.getResponse());
        assertEquals(content, response.getContent());
        assertEquals("txn-123", response.getResponse().getTraceId());
        assertEquals("2023-07-21T10:00:00Z", response.getResponse().getTimestamp());
        assertEquals("Sucesso", response.getResponse().getMessage());
    }

    @Test
    void deveCriarResponseModelApenasComConteudo() {
        String content = "Teste de conteúdo";

        ResponseModel<String> response = new ResponseModel<>(content);

        assertNotNull(response.getResponse());
        assertEquals(content, response.getContent());
        assertNotNull(response.getResponse().getTraceId());
        assertNotNull(response.getResponse().getTimestamp());
        assertNull(response.getResponse().getMessage());
    }

    @Test
    void deveCriarResponseModelComConteudoNulo() {
        ResponseModel<String> response = new ResponseModel<>(null);

        assertNotNull(response.getResponse());
        assertNull(response.getContent());
    }

    @Test
    void deveManterTipoGenericoDoConteudo() {
        Integer numeroInteiro = 42;
        ResponseModel<Integer> response = new ResponseModel<>(numeroInteiro);

        assertNotNull(response.getResponse());
        assertEquals(42, response.getContent());
        assertInstanceOf(Integer.class, response.getContent());
    }

    @Test
    void devePreservarMetadataOriginal() {
        ResponseMetadata metadata = new ResponseMetadata("txn-456", "2023-07-21T11:00:00Z", "Processado");
        String content = "Dados processados";

        ResponseModel<String> response = new ResponseModel<>(metadata, content);

        assertSame(metadata, response.getResponse());
        assertEquals("txn-456", response.getResponse().getTraceId());
        assertEquals("2023-07-21T11:00:00Z", response.getResponse().getTimestamp());
        assertEquals("Processado", response.getResponse().getMessage());
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

        assertNotNull(response.getResponse());
        assertEquals(objeto, response.getContent());
        assertEquals("teste", response.getContent().getName());
        assertEquals(100, response.getContent().getValue());
    }

    @Test
    void deveGerarMetadataAutomaticaComTimestampValido() {
        String content = "Teste timestamp";

        ResponseModel<String> response = new ResponseModel<>(content);

        assertNotNull(response.getResponse().getTimestamp());
        assertFalse(response.getResponse().getTimestamp().isEmpty());
    }

    @Test
    void deveGerarTransactionIdUnicoAutomaticamente() {
        ResponseModel<String> response1 = new ResponseModel<>("Conteúdo 1");
        ResponseModel<String> response2 = new ResponseModel<>("Conteúdo 2");

        assertNotEquals(response1.getResponse().getTraceId(),
                       response2.getResponse().getTraceId());
    }
}
