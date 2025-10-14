package br.com.tp.lncr.commons.utils;

import br.com.tp.lncr.commons.integrations.IntegrationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IntegrationUtil {

    private static final Logger log = LoggerFactory.getLogger(IntegrationUtil.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static <T> HttpEntity<T> setRequestEntity(T dto){
        return new HttpEntity<T>(dto, setHeaders());
    }

    private static <T> HttpEntity<T> setRequestEntity(T dto, HttpHeaders customHeaders){
        return new HttpEntity<T>(dto, customHeaders);
    }

    public static HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return headers;
    }

    public static <T> String toJson(T dto) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter "+ dto.getClass().getSimpleName() + " para JSON: {}", e.getMessage());
            throw new IntegrationException("Erro ao converter "+ dto.getClass().getSimpleName() + " para JSON", 500);
        }
    }

    public static <T> void postForObject(String url, T dto) {
        RestTemplate restTemplate = new RestTemplate();
        CompletableFuture.runAsync(() -> {
       try {
                restTemplate.postForObject(url, IntegrationUtil.setRequestEntity(dto), String.class);
            } catch (Exception e) {
                throw new IntegrationException("Erro na integração com", 500);
            }
        });
    }

    public static <T> String postForObjectWithReturn(String url, T dto, HttpHeaders header) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<T> requestEntity = IntegrationUtil.setRequestEntity(dto);
        if (header != null){
            requestEntity = IntegrationUtil.setRequestEntity(dto,header);
        }
        try {
            return restTemplate.postForObject(url, requestEntity, String.class);
        } catch (Exception e) {
            throw new IntegrationException("Erro na integração" + e.getMessage(), 500);
        }
    }

    public static <T> T getForObject(String url, TypeReference<T> response){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> getObject = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        try {
            getObject = restTemplate.getForEntity(url, String.class);
        } catch (Exception e) {
            if (e instanceof HttpClientErrorException.NotFound) {
                throw new IntegrationException("Não encontrado registro para "+url, 404);
            }
        }
        if (getObject.getStatusCode() == HttpStatus.OK) {
            return getIntegrationContentList(getObject.getBody(), response);
        } else {
            throw new IntegrationException("Erro na integração com " + url + " - Status: " + getObject.getStatusCode(), getObject.getStatusCode().value());
        }
    }

    public static <T> T getForObject(String url, Class<T> response){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> getObject = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        try {
            getObject = restTemplate.getForEntity(url, String.class);
        } catch (Exception e) {
            if (e instanceof HttpClientErrorException.NotFound) {
                throw new IntegrationException("Não encontrado registro para "+url, 404);
            }
        }
        if (getObject.getStatusCode() == HttpStatus.OK) {
            return getIntegrationContent(getObject.getBody(), response);
        } else {
            throw new IntegrationException("Erro na integração com " + url + " - Status: " + getObject.getStatusCode(), getObject.getStatusCode().value());
        }
    }


    public static <T> T getIntegrationContent(String body, Class<T> classType){
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try {
            JsonNode root = mapper.readTree(body);
            JsonNode contentNode = root.path("_content");
            return mapper.treeToValue(contentNode, classType);
        } catch (JsonProcessingException e) {
            throw new IntegrationException("Erro ao mapear conteúdo na integração: " + classType.getSimpleName(),500);
        }
    }

    public static <T> T getIntegrationContentList(String body, TypeReference<T> typeReference){
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try {
            JsonNode root = mapper.readTree(body);
            JsonNode contentNode = root.path("_content");
            return mapper.readValue(contentNode.traverse(), typeReference);
        } catch (Exception e) {
            throw new IntegrationException("Erro ao mapear conteúdo na integração: " + typeReference.getType(), 500);
        }
    }

    public static <T> ResponseEntity<String> patchForObject(String url, T requestBody ) {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        try {
            return restTemplate.exchange(patchHttpEntity(url, requestBody), String.class);
        } catch (Exception e) {
            throw new IntegrationException("Erro na integração com " + url+"\n"+ e.getMessage(), 500);
        }
    }

    public static <T> RequestEntity<String> patchHttpEntity(String url, T requestBody) {
        return RequestEntity
                .method(HttpMethod.PATCH, URI.create(url))
                .headers(setHeaders())
                .body(toJson(requestBody));
    }


}
