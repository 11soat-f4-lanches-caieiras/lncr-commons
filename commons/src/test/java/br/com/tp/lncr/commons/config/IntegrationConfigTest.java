package br.com.tp.lncr.commons.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationConfigTest {

    @Test
    void deveDefinirEObterCustomersUrl() {
        IntegrationConfig config = new IntegrationConfig();
        String url = "http://localhost:8080/customers";

        config.setCustomersUrl(url);

        assertEquals(url, config.getCustomersUrl());
    }

    @Test
    void deveDefinirEObterCustomersListUrl() {
        IntegrationConfig config = new IntegrationConfig();
        String url = "http://localhost:8080/customers/list";

        config.setCustomersListUrl(url);

        assertEquals(url, config.getCustomersListUrl());
    }

    @Test
    void deveDefinirEObterCustomerOrdersUrl() {
        IntegrationConfig config = new IntegrationConfig();
        String url = "http://localhost:8080/orders";

        config.setCustomerOrdersUrl(url);

        assertEquals(url, config.getCustomerOrdersUrl());
    }

    @Test
    void deveDefinirEObterFoodItemsUrl() {
        IntegrationConfig config = new IntegrationConfig();
        String url = "http://localhost:8080/fooditems";

        config.setFoodItemsUrl(url);

        assertEquals(url, config.getFoodItemsUrl());
    }

    @Test
    void deveDefinirEObterFoodItemsListUrl() {
        IntegrationConfig config = new IntegrationConfig();
        String url = "http://localhost:8080/fooditems/list";

        config.setFoodItemsListUrl(url);

        assertEquals(url, config.getFoodItemsListUrl());
    }

    @Test
    void deveDefinirEObterKitchenOrdersUrl() {
        IntegrationConfig config = new IntegrationConfig();
        String url = "http://localhost:8080/kitchen/orders";

        config.setKitchenOrdersUrl(url);

        assertEquals(url, config.getKitchenOrdersUrl());
    }

    @Test
    void deveDefinirEObterPaymentsUrl() {
        IntegrationConfig config = new IntegrationConfig();
        String url = "http://localhost:8080/payments";

        config.setPaymentsUrl(url);

        assertEquals(url, config.getPaymentsUrl());
    }

    @Test
    void deveDefinirEObterNotificationUrl() {
        IntegrationConfig config = new IntegrationConfig();
        String url = "http://localhost:8080/notifications";

        config.setNotificationUrl(url);

        assertEquals(url, config.getNotificationUrl());
    }

    @Test
    void devePermitirUrlsNulas() {
        IntegrationConfig config = new IntegrationConfig();

        config.setCustomersUrl(null);
        config.setPaymentsUrl(null);

        assertNull(config.getCustomersUrl());
        assertNull(config.getPaymentsUrl());
    }

    @Test
    void devePermitirUrlsVazias() {
        IntegrationConfig config = new IntegrationConfig();

        config.setCustomersUrl("");
        config.setFoodItemsUrl("");

        assertEquals("", config.getCustomersUrl());
        assertEquals("", config.getFoodItemsUrl());
    }

    @Test
    void deveManterValoresIndependentes() {
        IntegrationConfig config = new IntegrationConfig();

        config.setCustomersUrl("http://customers");
        config.setPaymentsUrl("http://payments");
        config.setNotificationUrl("http://notifications");

        assertEquals("http://customers", config.getCustomersUrl());
        assertEquals("http://payments", config.getPaymentsUrl());
        assertEquals("http://notifications", config.getNotificationUrl());
    }

    @Test
    void devePermitirUrlsComParametros() {
        IntegrationConfig config = new IntegrationConfig();
        String urlWithParams = "http://localhost:8080/customers?active=true&page=1";

        config.setCustomersUrl(urlWithParams);

        assertEquals(urlWithParams, config.getCustomersUrl());
    }

    @Test
    void devePermitirUrlsComPortasPersonalizadas() {
        IntegrationConfig config = new IntegrationConfig();

        config.setCustomersUrl("http://localhost:9090/customers");
        config.setPaymentsUrl("http://localhost:8888/payments");

        assertEquals("http://localhost:9090/customers", config.getCustomersUrl());
        assertEquals("http://localhost:8888/payments", config.getPaymentsUrl());
    }

    @Test
    void devePermitirUrlsComHttps() {
        IntegrationConfig config = new IntegrationConfig();

        config.setCustomersUrl("https://api.example.com/customers");
        config.setPaymentsUrl("https://secure.payment.com/api");

        assertEquals("https://api.example.com/customers", config.getCustomersUrl());
        assertEquals("https://secure.payment.com/api", config.getPaymentsUrl());
    }
}

