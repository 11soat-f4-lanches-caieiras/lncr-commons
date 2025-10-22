package br.com.tp.lncr.commons.integrations.customer;

import br.com.tp.lncr.commons.utils.IntegrationUtil;
import br.com.tp.lncr.commons.config.IntegrationConfig;
import br.com.tp.lncr.commons.integrations.IntegrationMapper;
import br.com.tp.lncr.core.dtos.customer.CustomerDTO;
import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderCustomerDTO;
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

class CustomerIntegrationImplTest {

    private CustomerIntegrationImpl customerIntegration;
    private IntegrationConfig integrationConfig;
    private IntegrationMapper integrationMapper;

    @BeforeEach
    void setUp() {
        integrationConfig = mock(IntegrationConfig.class);
        integrationMapper = mock(IntegrationMapper.class);
        customerIntegration = new CustomerIntegrationImpl(integrationConfig, integrationMapper);
    }

    @Test
    void deveBuscarDetalhesCustomerComSucesso() {
        when(integrationConfig.getCustomersUrl()).thenReturn("http://customer-service");

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1);
        customerDTO.setName("João Silva");

        CustomerOrderCustomerDTO expectedResult = new CustomerOrderCustomerDTO(1, "João Silva");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(CustomerDTO.class)))
                    .thenReturn(customerDTO);

            when(integrationMapper.toCustomerOrderCustomerDTO(any(CustomerDTO.class)))
                    .thenReturn(expectedResult);

            CustomerOrderCustomerDTO result = customerIntegration.getCustomerDetails(1);

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("João Silva", result.getName());
            verify(integrationMapper).toCustomerOrderCustomerDTO(any(CustomerDTO.class));
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://customer-service/1"), eq(CustomerDTO.class)));
        }
    }

    @Test
    void deveBuscarDetalhesCustomerComIdZero() {
        when(integrationConfig.getCustomersUrl()).thenReturn("http://customer-service");

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(0);
        customerDTO.setName("Cliente Anônimo");

        CustomerOrderCustomerDTO expectedResult = new CustomerOrderCustomerDTO(0, "Cliente Anônimo");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(CustomerDTO.class)))
                    .thenReturn(customerDTO);

            when(integrationMapper.toCustomerOrderCustomerDTO(any(CustomerDTO.class)))
                    .thenReturn(expectedResult);

            CustomerOrderCustomerDTO result = customerIntegration.getCustomerDetails(0);

            assertNotNull(result);
            verify(integrationMapper).toCustomerOrderCustomerDTO(any(CustomerDTO.class));
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://customer-service/0"), eq(CustomerDTO.class)));
        }
    }

    @Test
    void deveBuscarListaDetalhesCustomersComSucesso() {
        when(integrationConfig.getCustomersListUrl()).thenReturn("http://customer-service");

        List<Integer> customerIds = Arrays.asList(1, 2, 3);

        CustomerDTO customer1 = new CustomerDTO();
        customer1.setId(1);
        customer1.setName("João Silva");

        CustomerDTO customer2 = new CustomerDTO();
        customer2.setId(2);
        customer2.setName("Maria Santos");

        CustomerDTO customer3 = new CustomerDTO();
        customer3.setId(3);
        customer3.setName("Pedro Oliveira");

        List<CustomerDTO> mockCustomers = Arrays.asList(customer1, customer2, customer3);

        CustomerOrderCustomerDTO customerOrderDTO = new CustomerOrderCustomerDTO(1, "João Silva");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), any(TypeReference.class)))
                    .thenReturn(mockCustomers);

            when(integrationMapper.toCustomerOrderCustomerDTO(any(CustomerDTO.class)))
                    .thenReturn(customerOrderDTO);

            List<CustomerOrderCustomerDTO> result = customerIntegration.getCustomerDetailsList(customerIds);

            assertNotNull(result);
            assertEquals(3, result.size());
            verify(integrationMapper, times(3)).toCustomerOrderCustomerDTO(any(CustomerDTO.class));
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://customer-service/1,2,3"), any(TypeReference.class)));
        }
    }

    @Test
    void deveBuscarListaDetalhesCustomersComListaVazia() {
        when(integrationConfig.getCustomersListUrl()).thenReturn("http://customer-service");

        List<Integer> customerIds = Collections.emptyList();
        List<CustomerDTO> mockCustomers = Collections.emptyList();

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), any(TypeReference.class)))
                    .thenReturn(mockCustomers);

            List<CustomerOrderCustomerDTO> result = customerIntegration.getCustomerDetailsList(customerIds);

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(integrationMapper, never()).toCustomerOrderCustomerDTO(any(CustomerDTO.class));
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://customer-service/"), any(TypeReference.class)));
        }
    }

    @Test
    void deveBuscarListaDetalhesCustomersComUmCustomer() {
        when(integrationConfig.getCustomersListUrl()).thenReturn("http://customer-service");

        List<Integer> customerIds = List.of(1);

        CustomerDTO customer = new CustomerDTO();
        customer.setId(1);
        customer.setName("Ana Costa");

        List<CustomerDTO> mockCustomers = List.of(customer);

        CustomerOrderCustomerDTO customerOrderDTO = new CustomerOrderCustomerDTO(1, "Ana Costa");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), any(TypeReference.class)))
                    .thenReturn(mockCustomers);

            when(integrationMapper.toCustomerOrderCustomerDTO(any(CustomerDTO.class)))
                    .thenReturn(customerOrderDTO);

            List<CustomerOrderCustomerDTO> result = customerIntegration.getCustomerDetailsList(customerIds);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Ana Costa", result.getFirst().getName());
            verify(integrationMapper).toCustomerOrderCustomerDTO(any(CustomerDTO.class));
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://customer-service/1"), any(TypeReference.class)));
        }
    }

    @Test
    void deveProcessarCustomersNulosCorretamente() {
        when(integrationConfig.getCustomersListUrl()).thenReturn("http://customer-service");

        List<Integer> customerIds = Arrays.asList(1, 2);

        CustomerDTO customer1 = new CustomerDTO();
        customer1.setId(1);
        CustomerDTO customer2 = new CustomerDTO();
        customer2.setId(2);

        List<CustomerDTO> mockCustomers = Arrays.asList(customer1, customer2);

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), any(TypeReference.class)))
                    .thenReturn(mockCustomers);

            when(integrationMapper.toCustomerOrderCustomerDTO(any(CustomerDTO.class)))
                    .thenReturn(null);

            List<CustomerOrderCustomerDTO> result = customerIntegration.getCustomerDetailsList(customerIds);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(Objects::isNull));
            verify(integrationMapper, times(2)).toCustomerOrderCustomerDTO(any(CustomerDTO.class));
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://customer-service/1,2"), any(TypeReference.class)));
        }
    }
}
