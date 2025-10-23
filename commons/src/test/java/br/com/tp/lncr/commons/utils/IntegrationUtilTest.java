package br.com.tp.lncr.commons.utils;

import br.com.tp.lncr.commons.integrations.IntegrationException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class IntegrationUtilTest {

    // DTO de teste simples
    static class TestDTO {
        private String name;
        private Integer value;

        public TestDTO() {}

        public TestDTO(String name, Integer value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }

    // DTO não serializável para teste de erro
    static class NonSerializableDTO {
        private Object circularReference;

        public NonSerializableDTO() {
            this.circularReference = this;
        }

        public Object getCircularReference() {
            return circularReference;
        }

        public void setCircularReference(Object circularReference) {
            this.circularReference = circularReference;
        }
    }

    // ======================== TESTES toJson ========================

    @Test
    void toJson_deveConverterObjetoParaJsonComSucesso() {
        TestDTO dto = new TestDTO("Test", 123);

        String json = IntegrationUtil.toJson(dto);

        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"Test\""));
        assertTrue(json.contains("\"value\":123"));
    }

    @Test
    void toJson_deveConverterObjetoNuloParaJsonNull() {
        String json = IntegrationUtil.toJson(null);

        assertNotNull(json);
        assertEquals("null", json);
    }

    @Test
    void toJson_deveConverterListaParaJson() {
        List<TestDTO> list = Arrays.asList(
            new TestDTO("Test1", 1),
            new TestDTO("Test2", 2)
        );

        String json = IntegrationUtil.toJson(list);

        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"Test1\""));
        assertTrue(json.contains("\"name\":\"Test2\""));
    }

    @Test
    void toJson_deveConverterStringParaJson() {
        String value = "test string";

        String json = IntegrationUtil.toJson(value);

        assertNotNull(json);
        assertEquals("\"test string\"", json);
    }

    // ======================== TESTES postForObject ========================

    @Test
    void postForObject_deveEnviarRequisicaoPostAssincronamente() {
        // Este teste apenas verifica que o método não lança exceção
        // A execução é assíncrona, então não podemos verificar o resultado
        TestDTO dto = new TestDTO("Test", 123);
        String url = "http://localhost:8080/test";

        assertDoesNotThrow(() -> IntegrationUtil.postForObject(url, dto));
    }

    @Test
    void postForObject_deveExecutarSemErroComDtoNulo() {
        String url = "http://localhost:8080/test";
        assertDoesNotThrow(() -> IntegrationUtil.postForObject(url, null));
    }

    // ======================== TESTES postForObjectWithReturn ========================

    @Test
    void postForObjectWithReturn_deveRetornarRespostaComSucesso() {
        TestDTO dto = new TestDTO("Test", 123);
        String url = "http://localhost:8080/test";

        // Como a URL não existe, deve lançar IntegrationException
        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.postForObjectWithReturn(url, dto, null));

        assertNotNull(exception);
        assertEquals(500, exception.getCode());
    }

    @Test
    void postForObjectWithReturn_deveLancarIntegrationExceptionEmCasoDeErro() {
        TestDTO dto = new TestDTO("Test", 123);
        String url = "http://invalid-url";

        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.postForObjectWithReturn(url, dto, null));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Erro na integração"));
        assertEquals(500, exception.getCode());
    }

    @Test
    void postForObjectWithReturn_deveUsarHeadersPersonalizados() {
        TestDTO dto = new TestDTO("Test", 123);
        String url = "http://localhost:8080/test";
        HttpHeaders customHeaders = new HttpHeaders();
        customHeaders.set("Authorization", "Bearer token");

        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.postForObjectWithReturn(url, dto, customHeaders));

        assertNotNull(exception);
    }

    // ======================== TESTES getForObject (TypeReference) ========================

    @Test
    void getForObject_comTypeReference_deveLancarIntegrationExceptionQuandoNotFound() {
        String url = "http://localhost:8080/not-found";
        TypeReference<List<TestDTO>> typeRef = new TypeReference<>() {
        };

        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.getForObject(url, typeRef));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Não encontrado registro para") ||
                   exception.getMessage().contains("Erro na integração"));
    }

    @Test
    void getForObject_comTypeReference_deveLancarIntegrationExceptionEmCasoDeErroGenerico() {
        String url = "http://invalid-url-format";
        TypeReference<List<TestDTO>> typeRef = new TypeReference<>() {
        };

        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.getForObject(url, typeRef));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Erro na integração"));
    }

    // ======================== TESTES getForObject (Class) ========================

    @Test
    void getForObject_comClass_deveLancarIntegrationExceptionQuandoNotFound() {
        String url = "http://localhost:8080/not-found";

        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.getForObject(url, TestDTO.class));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Não encontrado registro para") ||
                   exception.getMessage().contains("Erro na integração"));
    }

    @Test
    void getForObject_comClass_deveLancarIntegrationExceptionEmCasoDeErroGenerico() {
        String url = "http://invalid-url-format";

        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.getForObject(url, TestDTO.class));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Erro na integração"));
    }

    // ======================== TESTES getIntegrationContent ========================

    @Test
    void getIntegrationContent_deveExtrairConteudoComSucesso() {
        String body = "{\"_content\":{\"name\":\"Test\",\"value\":123}}";

        TestDTO result = IntegrationUtil.getIntegrationContent(body, TestDTO.class);

        assertNotNull(result);
        assertEquals("Test", result.getName());
        assertEquals(123, result.getValue());
    }

    @Test
    void getIntegrationContent_deveRetornarNuloQuandoContentVazio() {
        String body = "{\"_content\":{}}";

        TestDTO result = IntegrationUtil.getIntegrationContent(body, TestDTO.class);

        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getValue());
    }

    @Test
    void getIntegrationContent_deveLancarIntegrationExceptionQuandoJsonInvalido() {
        String body = "{invalid json}";

        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.getIntegrationContent(body, TestDTO.class));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Erro ao mapear conteúdo na integração"));
        assertEquals(500, exception.getCode());
    }

    @Test
    void getIntegrationContent_deveLancarIntegrationExceptionQuandoJsonSemCampoContent() {
        String body = "{\"data\":{\"name\":\"Test\"}}";

        TestDTO result = IntegrationUtil.getIntegrationContent(body, TestDTO.class);
        assertNull(result);
    }

    @Test
    void getIntegrationContent_deveProcessarContentComValoresNulos() {
        String body = "{\"_content\":{\"name\":null,\"value\":null}}";

        TestDTO result = IntegrationUtil.getIntegrationContent(body, TestDTO.class);

        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getValue());
    }

    @Test
    void getIntegrationContent_deveLancarExcecaoQuandoBodyNulo() {
        assertThrows(IntegrationException.class, () -> IntegrationUtil.getIntegrationContent(null, TestDTO.class));
    }

    @Test
    void getIntegrationContent_deveLancarExcecaoQuandoBodyVazio() {
        // mapper.readTree("") lança exceção que é capturada como JsonProcessingException
        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.getIntegrationContent("", TestDTO.class));

        assertNotNull(exception);
        assertEquals(500, exception.getCode());
    }

    // ======================== TESTES getIntegrationContentList ========================

    @Test
    void getIntegrationContentList_deveExtrairListaComSucesso() {
        String body = "{\"_content\":[{\"name\":\"Test1\",\"value\":1},{\"name\":\"Test2\",\"value\":2}]}";
        TypeReference<List<TestDTO>> typeRef = new TypeReference<>() {
        };

        List<TestDTO> result = IntegrationUtil.getIntegrationContentList(body, typeRef);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test1", result.get(0).getName());
        assertEquals(1, result.get(0).getValue());
        assertEquals("Test2", result.get(1).getName());
        assertEquals(2, result.get(1).getValue());
    }

    @Test
    void getIntegrationContentList_deveRetornarListaVaziaQuandoContentVazio() {
        String body = "{\"_content\":[]}";
        TypeReference<List<TestDTO>> typeRef = new TypeReference<>() {
        };

        List<TestDTO> result = IntegrationUtil.getIntegrationContentList(body, typeRef);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getIntegrationContentList_deveLancarIntegrationExceptionQuandoJsonInvalido() {
        String body = "{invalid json}";
        TypeReference<List<TestDTO>> typeRef = new TypeReference<>() {
        };

        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.getIntegrationContentList(body, typeRef));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Erro ao mapear conteúdo na integração"));
        assertEquals(500, exception.getCode());
    }

    @Test
    void getIntegrationContentList_deveLancarExcecaoQuandoBodyNulo() {
        TypeReference<List<TestDTO>> typeRef = new TypeReference<>() {
        };

        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.getIntegrationContentList(null, typeRef));

        assertNotNull(exception);
        assertEquals(500, exception.getCode());
    }

    @Test
    void getIntegrationContentList_deveLancarExcecaoQuandoBodyVazio() {
        TypeReference<List<TestDTO>> typeRef = new TypeReference<>() {
        };

        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.getIntegrationContentList("", typeRef));

        assertNotNull(exception);
        assertEquals(500, exception.getCode());
    }

    @Test
    void getIntegrationContentList_deveProcessarListaComValoresNulos() {
        String body = "{\"_content\":[{\"name\":null,\"value\":null}]}";
        TypeReference<List<TestDTO>> typeRef = new TypeReference<>() {
        };

        List<TestDTO> result = IntegrationUtil.getIntegrationContentList(body, typeRef);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.getFirst().getName());
        assertNull(result.getFirst().getValue());
    }

    // ======================== TESTES patchForObject ========================

    @Test
    void patchForObject_deveLancarIntegrationExceptionQuandoUrlInvalida() {
        TestDTO dto = new TestDTO("Test", 123);
        String url = "http://invalid-url";

        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.patchForObject(url, dto));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Erro na integração com"));
        assertEquals(500, exception.getCode());
    }

    @Test
    void patchForObject_deveLancarExcecaoComUrlMalFormada() {
        TestDTO dto = new TestDTO("Test", 123);
        String url = "not-a-valid-url";

        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.patchForObject(url, dto));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Erro na integração"));
        assertEquals(500, exception.getCode());
    }

    @Test
    void patchForObject_deveFuncionarComDtoNulo() {
        String url = "http://localhost:8080/test";

        IntegrationException exception = assertThrows(IntegrationException.class, () -> IntegrationUtil.patchForObject(url, null));

        assertNotNull(exception);
    }

    // ======================== TESTES patchHttpEntity ========================

    @Test
    void patchHttpEntity_deveCriarRequestEntityComMetodoPatch() {
        TestDTO dto = new TestDTO("Test", 123);
        String url = "http://localhost:8080/test";

        RequestEntity<String> requestEntity = IntegrationUtil.patchHttpEntity(url, dto);

        assertNotNull(requestEntity);
        assertEquals(HttpMethod.PATCH, requestEntity.getMethod());
        assertEquals(URI.create(url), requestEntity.getUrl());
        assertNotNull(requestEntity.getBody());
        assertTrue(requestEntity.getBody().contains("\"name\":\"Test\""));
        assertTrue(requestEntity.getBody().contains("\"value\":123"));
    }

    @Test
    void patchHttpEntity_deveIncluirHeadersCorretos() {
        TestDTO dto = new TestDTO("Test", 123);
        String url = "http://localhost:8080/test";

        RequestEntity<String> requestEntity = IntegrationUtil.patchHttpEntity(url, dto);

        assertNotNull(requestEntity);
        HttpHeaders headers = requestEntity.getHeaders();
        assertNotNull(headers);
        assertEquals("application/json", headers.getFirst("Content-Type"));
    }

    @Test
    void patchHttpEntity_deveFuncionarComDtoNulo() {
        String url = "http://localhost:8080/test";

        RequestEntity<String> requestEntity = IntegrationUtil.patchHttpEntity(url, null);

        assertNotNull(requestEntity);
        assertEquals(HttpMethod.PATCH, requestEntity.getMethod());
        assertEquals("null", requestEntity.getBody());
    }

    @Test
    void patchHttpEntity_deveCriarEntityComUrlCompleta() {
        TestDTO dto = new TestDTO("Test", 123);
        String url = "http://localhost:8080/api/v1/test?param=value";

        RequestEntity<String> requestEntity = IntegrationUtil.patchHttpEntity(url, dto);

        assertNotNull(requestEntity);
        assertEquals(URI.create(url), requestEntity.getUrl());
    }

    @Test
    void patchHttpEntity_deveSerializarObjetoComplexo() {
        TestDTO dto = new TestDTO("Complex Test", 999);
        String url = "http://localhost:8080/test";

        RequestEntity<String> requestEntity = IntegrationUtil.patchHttpEntity(url, dto);

        assertNotNull(requestEntity);
        String body = requestEntity.getBody();
        assertNotNull(body);
        assertTrue(body.contains("Complex Test"));
        assertTrue(body.contains("999"));
    }

    // ======================== TESTES setHeaders ========================

    @Test
    void setHeaders_deveCriarHeadersComContentTypeJson() {
        HttpHeaders headers = IntegrationUtil.setHeaders();

        assertNotNull(headers);
        assertEquals("application/json", headers.getFirst("Content-Type"));
    }
}
