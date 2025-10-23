package br.com.tp.lncr.commons.integrations.customerorder;

import br.com.tp.lncr.commons.utils.IntegrationUtil;
import br.com.tp.lncr.commons.config.IntegrationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class CustomerOrderIntegrationImplTest {

    private CustomerOrderIntegrationImpl customerOrderIntegration;
    private IntegrationConfig integrationConfig;

    @BeforeEach
    void setUp() {
        integrationConfig = mock(IntegrationConfig.class);
        customerOrderIntegration = new CustomerOrderIntegrationImpl(integrationConfig);
    }

    @Test
    void deveAtualizarStatusCustomerOrderComSucesso() {
        when(integrationConfig.getCustomerOrdersUrl()).thenReturn("http://customerorder-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            ResponseEntity<String> mockResponse = new ResponseEntity<>("Success", HttpStatus.OK);
            mockedUtil.when(() -> IntegrationUtil.patchForObject(anyString(), any())).thenReturn(mockResponse);

            assertDoesNotThrow(() -> customerOrderIntegration.updateCustomerOrderStatus(123, "RECEIVED"));

            mockedUtil.verify(() -> IntegrationUtil.patchForObject(eq("http://customerorder-service/123/updateStatus/RECEIVED"), isNull()));
        }
    }

    @Test
    void deveAtualizarStatusCustomerOrderParaInProgress() {
        when(integrationConfig.getCustomerOrdersUrl()).thenReturn("http://customerorder-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            ResponseEntity<String> mockResponse = new ResponseEntity<>("Success", HttpStatus.OK);
            mockedUtil.when(() -> IntegrationUtil.patchForObject(anyString(), any())).thenReturn(mockResponse);

            assertDoesNotThrow(() -> customerOrderIntegration.updateCustomerOrderStatus(456, "PREPARING"));

            mockedUtil.verify(() -> IntegrationUtil.patchForObject(eq("http://customerorder-service/456/updateStatus/PREPARING"), isNull()));
        }
    }

    @Test
    void deveAtualizarStatusCustomerOrderParaReady() {
        when(integrationConfig.getCustomerOrdersUrl()).thenReturn("http://customerorder-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            ResponseEntity<String> mockResponse = new ResponseEntity<>("Success", HttpStatus.OK);
            mockedUtil.when(() -> IntegrationUtil.patchForObject(anyString(), any())).thenReturn(mockResponse);

            assertDoesNotThrow(() -> customerOrderIntegration.updateCustomerOrderStatus(789, "READY"));

            mockedUtil.verify(() -> IntegrationUtil.patchForObject(eq("http://customerorder-service/789/updateStatus/READY"), isNull()));
        }
    }

    @Test
    void deveAtualizarStatusCustomerOrderParaDelivered() {
        when(integrationConfig.getCustomerOrdersUrl()).thenReturn("http://customerorder-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            ResponseEntity<String> mockResponse = new ResponseEntity<>("Success", HttpStatus.OK);
            mockedUtil.when(() -> IntegrationUtil.patchForObject(anyString(), any())).thenReturn(mockResponse);

            assertDoesNotThrow(() -> customerOrderIntegration.updateCustomerOrderStatus(101, "FINISHED"));

            mockedUtil.verify(() -> IntegrationUtil.patchForObject(eq("http://customerorder-service/101/updateStatus/FINISHED"), isNull()));
        }
    }

    @Test
    void deveAtualizarStatusCustomerOrderParaCancelled() {
        when(integrationConfig.getCustomerOrdersUrl()).thenReturn("http://customerorder-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            ResponseEntity<String> mockResponse = new ResponseEntity<>("Success", HttpStatus.OK);
            mockedUtil.when(() -> IntegrationUtil.patchForObject(anyString(), any())).thenReturn(mockResponse);

            assertDoesNotThrow(() -> customerOrderIntegration.updateCustomerOrderStatus(202, "CANCELLED"));

            mockedUtil.verify(() -> IntegrationUtil.patchForObject(eq("http://customerorder-service/202/updateStatus/CANCELLED"), isNull()));
        }
    }

    @Test
    void deveAtualizarStatusComIdZero() {
        when(integrationConfig.getCustomerOrdersUrl()).thenReturn("http://customerorder-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            ResponseEntity<String> mockResponse = new ResponseEntity<>("Success", HttpStatus.OK);
            mockedUtil.when(() -> IntegrationUtil.patchForObject(anyString(), any())).thenReturn(mockResponse);

            assertDoesNotThrow(() -> customerOrderIntegration.updateCustomerOrderStatus(0, "RECEIVED"));

            mockedUtil.verify(() -> IntegrationUtil.patchForObject(eq("http://customerorder-service/0/updateStatus/RECEIVED"), isNull()));
        }
    }

    @Test
    void deveAtualizarStatusComStatusNulo() {
        when(integrationConfig.getCustomerOrdersUrl()).thenReturn("http://customerorder-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            ResponseEntity<String> mockResponse = new ResponseEntity<>("Success", HttpStatus.OK);
            mockedUtil.when(() -> IntegrationUtil.patchForObject(anyString(), any())).thenReturn(mockResponse);

            assertDoesNotThrow(() -> customerOrderIntegration.updateCustomerOrderStatus(123, null));

            mockedUtil.verify(() -> IntegrationUtil.patchForObject(eq("http://customerorder-service/123/updateStatus/null"), isNull()));
        }
    }
}
