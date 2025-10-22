package br.com.tp.lncr.commons.model;

import br.com.tp.lncr.core.model.ResponseMetadata;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseListModelTest {

    @Test
    void deveCriarResponseListModelComMetadataELista() {
        ResponseMetadata metadata = new ResponseMetadata("txn-123", "2023-07-21T10:00:00Z", "Lista recuperada");
        List<String> content = Arrays.asList("Item1", "Item2", "Item3");

        ResponseListModel<String> response = new ResponseListModel<>(metadata, content);

        assertEquals(metadata, response.getResponse());
        assertEquals(content, response.getContent());
        assertEquals(3, response.getContent().size());
        assertEquals("Item1", response.getContent().getFirst());
    }

    @Test
    void deveCriarResponseListModelApenasComLista() {
        List<String> content = Arrays.asList("Item1", "Item2");

        ResponseListModel<String> response = new ResponseListModel<>(content);

        assertNotNull(response.getResponse());
        assertEquals(content, response.getContent());
        assertEquals(2, response.getContent().size());
        assertNotNull(response.getResponse().getTraceId());
        assertNotNull(response.getResponse().getTimestamp());
    }

    @Test
    void deveCriarResponseListModelComListaVazia() {
        List<String> content = Collections.emptyList();

        ResponseListModel<String> response = new ResponseListModel<>(content);
        int responseSize = response.getContent().size();
        assertNotNull(response.getResponse());
        assertEquals(content, response.getContent());
        assertTrue(response.getContent().isEmpty());
        assertEquals(0, responseSize);
    }

    @Test
    void deveCriarResponseListModelComListaNula() {
        ResponseListModel<String> response = new ResponseListModel<>(null);

        assertNotNull(response.getResponse());
        assertNull(response.getContent());
    }

    @Test
    void deveManterTipoGenericoDaLista() {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5);

        ResponseListModel<Integer> response = new ResponseListModel<>(numeros);

        assertNotNull(response.getResponse());
        assertEquals(5, response.getContent().size());
        assertInstanceOf(Integer.class, response.getContent().get(0));
        assertEquals(1, response.getContent().get(0));
        assertEquals(5, response.getContent().get(4));
    }

    @Test
    void devePreservarMetadataOriginal() {
        ResponseMetadata metadata = new ResponseMetadata("txn-456", "2023-07-21T11:00:00Z", "Dados listados");
        List<String> content = Arrays.asList("A", "B", "C");

        ResponseListModel<String> response = new ResponseListModel<>(metadata, content);

        assertSame(metadata, response.getResponse());
        assertEquals("txn-456", response.getResponse().getTraceId());
        assertEquals("2023-07-21T11:00:00Z", response.getResponse().getTimestamp());
        assertEquals("Dados listados", response.getResponse().getMessage());
    }

    @Test
    void devePermitirListaDeObjetosComplexos() {
        class TestObject {
            private final String name;
            private final int id;

            public TestObject(String name, int id) {
                this.name = name;
                this.id = id;
            }

            public String getName() { return name; }
            public int getId() { return id; }
        }

        List<TestObject> objetos = Arrays.asList(
            new TestObject("Objeto1", 1),
            new TestObject("Objeto2", 2)
        );

        ResponseListModel<TestObject> response = new ResponseListModel<>(objetos);

        assertNotNull(response.getResponse());
        assertEquals(2, response.getContent().size());
        assertEquals("Objeto1", response.getContent().get(0).getName());
        assertEquals(2, response.getContent().get(1).getId());
    }

    @Test
    void deveGerarMetadataAutomaticaComTimestampValido() {
        List<String> content = List.of("teste");

        ResponseListModel<String> response = new ResponseListModel<>(content);

        assertNotNull(response.getResponse().getTimestamp());
        assertFalse(response.getResponse().getTimestamp().isEmpty());
    }

    @Test
    void deveGerarTransactionIdUnicoAutomaticamente() {
        List<String> lista1 = List.of("A");
        List<String> lista2 = List.of("B");

        ResponseListModel<String> response1 = new ResponseListModel<>(lista1);
        ResponseListModel<String> response2 = new ResponseListModel<>(lista2);

        assertNotEquals(response1.getResponse().getTraceId(),
                       response2.getResponse().getTraceId());
    }

    @Test
    void deveManterReferenciaDaListaOriginal() {
        List<String> listaOriginal = Arrays.asList("Item1", "Item2");

        ResponseListModel<String> response = new ResponseListModel<>(listaOriginal);

        assertSame(listaOriginal, response.getContent());
    }

    @Test
    void devePermitirListaComUmUnicoItem() {
        List<String> content = List.of("ÚnicoItem");

        ResponseListModel<String> response = new ResponseListModel<>(content);

        assertNotNull(response.getResponse());
        assertEquals(1, response.getContent().size());
        assertEquals("ÚnicoItem", response.getContent().getFirst());
    }
}
