package br.com.tp.lncr.commons.integrations.fooditem;

import br.com.tp.lncr.commons.utils.IntegrationUtil;
import br.com.tp.lncr.commons.config.IntegrationConfig;
import br.com.tp.lncr.commons.integrations.IntegrationMapper;
import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderFoodItemDTO;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoodItemIntegrationImplTest {

    private FoodItemIntegrationImpl foodItemIntegration;
    private IntegrationConfig integrationConfig;
    private IntegrationMapper integrationMapper;

    @BeforeEach
    void setUp() {
        integrationConfig = mock(IntegrationConfig.class);
        integrationMapper = mock(IntegrationMapper.class);
        foodItemIntegration = new FoodItemIntegrationImpl(integrationConfig, integrationMapper);
    }

    @Test
    void deveBuscarDetalhesFoodItemsComSucesso() {
        when(integrationConfig.getFoodItemsUrl()).thenReturn("http://fooditem-service");

        List<Integer> foodItemIds = Arrays.asList(1, 2, 3);

        FoodItemDTO foodItem1 = new FoodItemDTO();
        foodItem1.setId(1);
        foodItem1.setName("Hambúrguer");

        FoodItemDTO foodItem2 = new FoodItemDTO();
        foodItem2.setId(2);
        foodItem2.setName("Pizza");

        FoodItemDTO foodItem3 = new FoodItemDTO();
        foodItem3.setId(3);
        foodItem3.setName("Batata Frita");

        List<FoodItemDTO> mockFoodItems = Arrays.asList(foodItem1, foodItem2, foodItem3);

        CustomerOrderFoodItemDTO customerOrderFoodItem = new CustomerOrderFoodItemDTO();
        customerOrderFoodItem.setId(1);
        customerOrderFoodItem.setName("Hambúrguer");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), any(TypeReference.class)))
                    .thenReturn(mockFoodItems);

            when(integrationMapper.toCustomerOrderFoodItemDTO(any(FoodItemDTO.class)))
                    .thenReturn(customerOrderFoodItem);

            List<CustomerOrderFoodItemDTO> result = foodItemIntegration.getFoodItemDetailList(foodItemIds);

            assertNotNull(result);
            assertEquals(3, result.size());
            verify(integrationMapper, times(3)).toCustomerOrderFoodItemDTO(any(FoodItemDTO.class));
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://fooditem-service/1,2,3"), any(TypeReference.class)));
        }
    }

    @Test
    void deveBuscarDetalhesFoodItemsComListaVazia() {
        when(integrationConfig.getFoodItemsUrl()).thenReturn("http://fooditem-service");

        List<Integer> foodItemIds = Collections.emptyList();
        List<FoodItemDTO> mockFoodItems = Collections.emptyList();

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), any(TypeReference.class)))
                    .thenReturn(mockFoodItems);

            List<CustomerOrderFoodItemDTO> result = foodItemIntegration.getFoodItemDetailList(foodItemIds);

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(integrationMapper, never()).toCustomerOrderFoodItemDTO(any(FoodItemDTO.class));
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://fooditem-service/"), any(TypeReference.class)));
        }
    }

    @Test
    void deveBuscarDetalhesFoodItemsComUmItem() {
        when(integrationConfig.getFoodItemsUrl()).thenReturn("http://fooditem-service");

        List<Integer> foodItemIds = List.of(1);

        FoodItemDTO foodItem = new FoodItemDTO();
        foodItem.setId(1);
        foodItem.setName("Pizza");

        List<FoodItemDTO> mockFoodItems = List.of(foodItem);

        CustomerOrderFoodItemDTO customerOrderFoodItem = new CustomerOrderFoodItemDTO();
        customerOrderFoodItem.setId(1);
        customerOrderFoodItem.setName("Pizza");
        customerOrderFoodItem.setPrice(35.90);

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), any(TypeReference.class)))
                    .thenReturn(mockFoodItems);

            when(integrationMapper.toCustomerOrderFoodItemDTO(any(FoodItemDTO.class)))
                    .thenReturn(customerOrderFoodItem);

            List<CustomerOrderFoodItemDTO> result = foodItemIntegration.getFoodItemDetailList(foodItemIds);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Pizza", result.getFirst().getName());
            verify(integrationMapper).toCustomerOrderFoodItemDTO(any(FoodItemDTO.class));
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://fooditem-service/1"), any(TypeReference.class)));
        }
    }

    @Test
    void deveProcessarFoodItemsNulosCorretamente() {
        when(integrationConfig.getFoodItemsUrl()).thenReturn("http://fooditem-service");

        List<Integer> foodItemIds = Arrays.asList(1, 2);

        FoodItemDTO foodItem1 = new FoodItemDTO();
        foodItem1.setId(1);
        FoodItemDTO foodItem2 = new FoodItemDTO();
        foodItem2.setId(2);

        List<FoodItemDTO> mockFoodItems = Arrays.asList(foodItem1, foodItem2);

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), any(TypeReference.class)))
                    .thenReturn(mockFoodItems);

            when(integrationMapper.toCustomerOrderFoodItemDTO(any(FoodItemDTO.class)))
                    .thenReturn(null);

            List<CustomerOrderFoodItemDTO> result = foodItemIntegration.getFoodItemDetailList(foodItemIds);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(Objects::isNull));
            verify(integrationMapper, times(2)).toCustomerOrderFoodItemDTO(any(FoodItemDTO.class));
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://fooditem-service/1,2"), any(TypeReference.class)));
        }
    }
}
