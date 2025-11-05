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

            CustomerOrderCustomerDTO result = integrationMapper.toCustomerOrderCustomerDTO(customerIntegration.getCustomerDetails(1));

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("João Silva", result.getName());
            verify(integrationMapper).toCustomerOrderCustomerDTO(any(CustomerDTO.class));
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://customer-service/1"), eq(CustomerDTO.class)));
        }
    }

    @Test
    void deveBuscarCustomerDetailsComSucesso() {
        when(integrationConfig.getCustomersUrl()).thenReturn("http://customer-service");

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1);
        customerDTO.setName("João Silva");
        customerDTO.setEmail("joao.silva@email.com");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(CustomerDTO.class)))
                    .thenReturn(customerDTO);

            CustomerDTO result = customerIntegration.getCustomerDetails(1);

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("João Silva", result.getName());
            assertEquals("joao.silva@email.com", result.getEmail());
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://customer-service/1"), eq(CustomerDTO.class)));
        }
    }

    @Test
    void deveBuscarCustomerDetailsComIdZero() {
        when(integrationConfig.getCustomersUrl()).thenReturn("http://customer-service");

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(0);
        customerDTO.setName("Cliente Anônimo");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(CustomerDTO.class)))
                    .thenReturn(customerDTO);

            CustomerDTO result = customerIntegration.getCustomerDetails(0);

            assertNotNull(result);
            assertEquals(0, result.getId());
            assertEquals("Cliente Anônimo", result.getName());
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://customer-service/0"), eq(CustomerDTO.class)));
        }
    }

    @Test
    void deveBuscarCustomerDetailsComIdNegativo() {
        when(integrationConfig.getCustomersUrl()).thenReturn("http://customer-service");

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(-1);

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(CustomerDTO.class)))
                    .thenReturn(customerDTO);

            CustomerDTO result = customerIntegration.getCustomerDetails(-1);

            assertNotNull(result);
            assertEquals(-1, result.getId());
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://customer-service/-1"), eq(CustomerDTO.class)));
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

            CustomerOrderCustomerDTO result = integrationMapper.toCustomerOrderCustomerDTO(customerIntegration.getCustomerDetails(0));

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

    @Test
    void deveBuscarCustomerDetailsPorDocumentoComSucesso() {
        when(integrationConfig.getCustomersUrl()).thenReturn("http://customer-service");

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1);
        customerDTO.setName("João Silva");
        customerDTO.setDocumentNumber("12345678900");
        customerDTO.setEmail("joao.silva@email.com");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(CustomerDTO.class)))
                    .thenReturn(customerDTO);

            CustomerDTO result = customerIntegration.getCustomerDetailsByDocument("12345678900");

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("João Silva", result.getName());
            assertEquals("12345678900", result.getDocumentNumber());
            assertEquals("joao.silva@email.com", result.getEmail());
            mockedUtil.verify(() -> IntegrationUtil.getForObject(
                    eq("http://customer-service/documentNumber/12345678900"),
                    eq(CustomerDTO.class)));
        }
    }

    @Test
    void deveBuscarCustomerDetailsPorDocumentoCNPJ() {
        when(integrationConfig.getCustomersUrl()).thenReturn("http://customer-service");

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(2);
        customerDTO.setName("Empresa XYZ LTDA");
        customerDTO.setDocumentNumber("12345678000199");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(CustomerDTO.class)))
                    .thenReturn(customerDTO);

            CustomerDTO result = customerIntegration.getCustomerDetailsByDocument("12345678000199");

            assertNotNull(result);
            assertEquals(2, result.getId());
            assertEquals("Empresa XYZ LTDA", result.getName());
            assertEquals("12345678000199", result.getDocumentNumber());
            mockedUtil.verify(() -> IntegrationUtil.getForObject(
                    eq("http://customer-service/documentNumber/12345678000199"),
                    eq(CustomerDTO.class)));
        }
    }

    @Test
    void deveBuscarCustomerDetailsPorDocumentoComCaracteresEspeciais() {
        when(integrationConfig.getCustomersUrl()).thenReturn("http://customer-service");

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(3);
        customerDTO.setName("Maria Santos");
        customerDTO.setDocumentNumber("123.456.789-00");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(CustomerDTO.class)))
                    .thenReturn(customerDTO);

            CustomerDTO result = customerIntegration.getCustomerDetailsByDocument("123.456.789-00");

            assertNotNull(result);
            assertEquals(3, result.getId());
            assertEquals("Maria Santos", result.getName());
            assertEquals("123.456.789-00", result.getDocumentNumber());
            mockedUtil.verify(() -> IntegrationUtil.getForObject(
                    eq("http://customer-service/documentNumber/123.456.789-00"),
                    eq(CustomerDTO.class)));
        }
    }

    @Test
    void deveBuscarCustomerDetailsPorDocumentoVazio() {
        when(integrationConfig.getCustomersUrl()).thenReturn("http://customer-service");

        CustomerDTO customerDTO = new CustomerDTO();

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(CustomerDTO.class)))
                    .thenReturn(customerDTO);

            CustomerDTO result = customerIntegration.getCustomerDetailsByDocument("");

            assertNotNull(result);
            mockedUtil.verify(() -> IntegrationUtil.getForObject(
                    eq("http://customer-service/documentNumber/"),
                    eq(CustomerDTO.class)));
        }
    }

    @Test
    void deveBuscarCustomerDetailsPorDocumentoNulo() {
        when(integrationConfig.getCustomersUrl()).thenReturn("http://customer-service");

        CustomerDTO customerDTO = new CustomerDTO();

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(CustomerDTO.class)))
                    .thenReturn(customerDTO);

            CustomerDTO result = customerIntegration.getCustomerDetailsByDocument(null);

            assertNotNull(result);
            mockedUtil.verify(() -> IntegrationUtil.getForObject(
                    eq("http://customer-service/documentNumber/null"),
                    eq(CustomerDTO.class)));
        }
    }
}
