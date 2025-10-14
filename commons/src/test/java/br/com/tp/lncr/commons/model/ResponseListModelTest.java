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

        assertEquals(metadata, response.get_response());
        assertEquals(content, response.get_content());
        assertEquals(3, response.get_content().size());
        assertEquals("Item1", response.get_content().get(0));
    }

    @Test
    void deveCriarResponseListModelApenasComLista() {
        List<String> content = Arrays.asList("Item1", "Item2");

        ResponseListModel<String> response = new ResponseListModel<>(content);

        assertNotNull(response.get_response());
        assertEquals(content, response.get_content());
        assertEquals(2, response.get_content().size());
        assertNotNull(response.get_response().get_traceId());
        assertNotNull(response.get_response().get_timestamp());
    }

    @Test
    void deveCriarResponseListModelComListaVazia() {
        List<String> content = Collections.emptyList();

        ResponseListModel<String> response = new ResponseListModel<>(content);

        assertNotNull(response.get_response());
        assertEquals(content, response.get_content());
        assertTrue(response.get_content().isEmpty());
        assertEquals(0, response.get_content().size());
    }

    @Test
    void deveCriarResponseListModelComListaNula() {
        ResponseListModel<String> response = new ResponseListModel<>(null);

        assertNotNull(response.get_response());
        assertNull(response.get_content());
    }

    @Test
    void deveManterTipoGenericoDaLista() {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5);

        ResponseListModel<Integer> response = new ResponseListModel<>(numeros);

        assertNotNull(response.get_response());
        assertEquals(5, response.get_content().size());
        assertInstanceOf(Integer.class, response.get_content().get(0));
        assertEquals(1, response.get_content().get(0));
        assertEquals(5, response.get_content().get(4));
    }

    @Test
    void devePreservarMetadataOriginal() {
        ResponseMetadata metadata = new ResponseMetadata("txn-456", "2023-07-21T11:00:00Z", "Dados listados");
        List<String> content = Arrays.asList("A", "B", "C");

        ResponseListModel<String> response = new ResponseListModel<>(metadata, content);

        assertSame(metadata, response.get_response());
        assertEquals("txn-456", response.get_response().get_traceId());
        assertEquals("2023-07-21T11:00:00Z", response.get_response().get_timestamp());
        assertEquals("Dados listados", response.get_response().get_message());
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

        assertNotNull(response.get_response());
        assertEquals(2, response.get_content().size());
        assertEquals("Objeto1", response.get_content().get(0).getName());
        assertEquals(2, response.get_content().get(1).getId());
    }

    @Test
    void deveGerarMetadataAutomaticaComTimestampValido() {
        List<String> content = List.of("teste");

        ResponseListModel<String> response = new ResponseListModel<>(content);

        assertNotNull(response.get_response().get_timestamp());
        assertFalse(response.get_response().get_timestamp().isEmpty());
    }

    @Test
    void deveGerarTransactionIdUnicoAutomaticamente() {
        List<String> lista1 = List.of("A");
        List<String> lista2 = List.of("B");

        ResponseListModel<String> response1 = new ResponseListModel<>(lista1);
        ResponseListModel<String> response2 = new ResponseListModel<>(lista2);

        assertNotEquals(response1.get_response().get_traceId(),
                       response2.get_response().get_traceId());
    }

    @Test
    void deveManterReferenciaDaListaOriginal() {
        List<String> listaOriginal = Arrays.asList("Item1", "Item2");

        ResponseListModel<String> response = new ResponseListModel<>(listaOriginal);

        assertSame(listaOriginal, response.get_content());
    }

    @Test
    void devePermitirListaComUmUnicoItem() {
        List<String> content = List.of("ÚnicoItem");

        ResponseListModel<String> response = new ResponseListModel<>(content);

        assertNotNull(response.get_response());
        assertEquals(1, response.get_content().size());
        assertEquals("ÚnicoItem", response.get_content().get(0));
    }
}
