package br.com.tp.lncr.commons.integrations.kitchenorder;

import br.com.tp.lncr.commons.utils.IntegrationUtil;
import br.com.tp.lncr.commons.config.IntegrationConfig;
import br.com.tp.lncr.commons.integrations.IntegrationException;
import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderDTO;
import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderFoodItemDTO;
import br.com.tp.lncr.core.dtos.kitchenorder.KitchenOrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KitchenOrderIntegrationImplTest {

    private KitchenOrderIntegrationImpl kitchenOrderIntegration;
    private IntegrationConfig integrationConfig;

    @BeforeEach
    void setUp() {
        integrationConfig = mock(IntegrationConfig.class);
        kitchenOrderIntegration = new KitchenOrderIntegrationImpl(integrationConfig);
    }

    @Test
    void deveCriarKitchenOrderComSucesso() {
        when(integrationConfig.getKitchenOrdersUrl()).thenReturn("http://kitchen-service");

        CustomerOrderDTO customerOrderDTO = new CustomerOrderDTO();
        customerOrderDTO.setId(123);

        CustomerOrderFoodItemDTO foodItem = new CustomerOrderFoodItemDTO();
        foodItem.setName("Hambúrguer");
        foodItem.setDescription("Hambúrguer com queijo");
        foodItem.setNotes("Sem cebola");
        customerOrderDTO.setFoodItems(List.of(foodItem));

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.toJson(any())).thenReturn("{}");
            mockedUtil.when(() -> IntegrationUtil.postForObject(anyString(), any())).then(invocation -> null);

            assertDoesNotThrow(() -> kitchenOrderIntegration.createKitchenOrder(customerOrderDTO));

            mockedUtil.verify(() -> IntegrationUtil.postForObject(eq("http://kitchen-service"), any()));
        }
    }

    @Test
    void deveCriarKitchenOrderComListaVazia() {
        when(integrationConfig.getKitchenOrdersUrl()).thenReturn("http://kitchen-service");

        CustomerOrderDTO customerOrderDTO = new CustomerOrderDTO();
        customerOrderDTO.setId(123);
        customerOrderDTO.setFoodItems(Collections.emptyList());

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.toJson(any())).thenReturn("{}");
            mockedUtil.when(() -> IntegrationUtil.postForObject(anyString(), any())).then(invocation -> null);

            assertDoesNotThrow(() -> kitchenOrderIntegration.createKitchenOrder(customerOrderDTO));

            mockedUtil.verify(() -> IntegrationUtil.postForObject(eq("http://kitchen-service"), any()));
        }
    }

    @Test
    void deveCancelarKitchenOrderPorId() {
        when(integrationConfig.getKitchenOrdersUrl()).thenReturn("http://kitchen-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            ResponseEntity<String> mockResponse = new ResponseEntity<>("Success", HttpStatus.OK);
            mockedUtil.when(() -> IntegrationUtil.patchForObject(anyString(), any())).thenReturn(mockResponse);

            assertDoesNotThrow(() -> kitchenOrderIntegration.cancelKitchenOrderById(123));

            mockedUtil.verify(() -> IntegrationUtil.patchForObject(eq("http://kitchen-service/123/updateStatus/Cancelled?forceUpdate=true"), isNull()));
        }
    }

    @Test
    void deveBuscarKitchenOrderPorCustomerOrderId() {
        when(integrationConfig.getKitchenOrdersUrl()).thenReturn("http://kitchen-service");
        KitchenOrderDTO expectedKitchenOrder = new KitchenOrderDTO();
        expectedKitchenOrder.setCustomerOrderId(123);

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(KitchenOrderDTO.class)))
                    .thenReturn(expectedKitchenOrder);

            KitchenOrderDTO result = kitchenOrderIntegration.getKitchenOrderByCustomerOrderId(123);

            assertNotNull(result);
            assertEquals(123, result.getCustomerOrderId());
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://kitchen-service/customerOrder/123"), eq(KitchenOrderDTO.class)));
        }
    }

    @Test
    void deveAtualizarStatusKitchenOrder() {
        when(integrationConfig.getKitchenOrdersUrl()).thenReturn("http://kitchen-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            ResponseEntity<String> mockResponse = new ResponseEntity<>("Success", HttpStatus.OK);
            mockedUtil.when(() -> IntegrationUtil.patchForObject(anyString(), any())).thenReturn(mockResponse);

            assertDoesNotThrow(() -> kitchenOrderIntegration.updateKitchenOrderById(123, "PREPARING", false));

            mockedUtil.verify(() -> IntegrationUtil.patchForObject(eq("http://kitchen-service/123/updateStatus/PREPARING?forceUpdate=false"), isNull()));
        }
    }

    @Test
    void deveAtualizarStatusKitchenOrderComForceUpdate() {
        when(integrationConfig.getKitchenOrdersUrl()).thenReturn("http://kitchen-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            ResponseEntity<String> mockResponse = new ResponseEntity<>("Success", HttpStatus.OK);
            mockedUtil.when(() -> IntegrationUtil.patchForObject(anyString(), any())).thenReturn(mockResponse);

            assertDoesNotThrow(() -> kitchenOrderIntegration.updateKitchenOrderById(123, "READY", true));

            mockedUtil.verify(() -> IntegrationUtil.patchForObject(eq("http://kitchen-service/123/updateStatus/READY?forceUpdate=true"), isNull()));
        }
    }

    @Test
    void deveLancarExcecaoQuandoKitchenOrderNaoForEncontrado() {
        when(integrationConfig.getKitchenOrdersUrl()).thenReturn("http://kitchen-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(KitchenOrderDTO.class)))
                    .thenThrow(new IntegrationException("Erro na integração com http://kitchen-service/customerOrder/999 - Status: 404 NOT_FOUND", 404));

            assertThrows(IntegrationException.class, () -> kitchenOrderIntegration.getKitchenOrderByCustomerOrderId(999));

            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://kitchen-service/customerOrder/999"), eq(KitchenOrderDTO.class)));
        }
    }
}
