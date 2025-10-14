package br.com.tp.lncr.commons.integrations;

import br.com.tp.lncr.core.dtos.customer.CustomerDTO;
import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderCustomerDTO;
import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderFoodItemDTO;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationMapperTest {

    private IntegrationMapper integrationMapper;

    @BeforeEach
    void setUp() {
        integrationMapper = new IntegrationMapper();
    }

    @Test
    void deveConverterFoodItemDTOParaCustomerOrderFoodItemDTO() {
        FoodItemDTO foodItemDTO = new FoodItemDTO();
        foodItemDTO.setId(1);
        foodItemDTO.setDescription("Hambúrguer delicioso");
        foodItemDTO.setName("Big Burger");
        foodItemDTO.setPrice(25.90);

        CustomerOrderFoodItemDTO result = integrationMapper.toCustomerOrderFoodItemDTO(foodItemDTO);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Hambúrguer delicioso", result.getDescription());
        assertEquals("Big Burger", result.getName());
        assertEquals(25.90, result.getPrice());
    }

    @Test
    void deveRetornarNullQuandoFoodItemDTOForNull() {
        CustomerOrderFoodItemDTO result = integrationMapper.toCustomerOrderFoodItemDTO(null);

        assertNull(result);
    }

    @Test
    void deveConverterCustomerDTOParaCustomerOrderCustomerDTO() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1);
        customerDTO.setName("João Silva");

        CustomerOrderCustomerDTO result = integrationMapper.toCustomerOrderCustomerDTO(customerDTO);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("João Silva", result.getName());
    }

    @Test
    void deveRetornarNullQuandoCustomerDTOForNull() {
        CustomerOrderCustomerDTO result = integrationMapper.toCustomerOrderCustomerDTO(null);

        assertNull(result);
    }
}
