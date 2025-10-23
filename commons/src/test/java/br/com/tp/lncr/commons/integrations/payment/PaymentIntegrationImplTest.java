package br.com.tp.lncr.commons.integrations.payment;

import br.com.tp.lncr.commons.utils.IntegrationUtil;
import br.com.tp.lncr.commons.config.IntegrationConfig;
import br.com.tp.lncr.commons.integrations.IntegrationException;
import br.com.tp.lncr.core.dtos.payment.PaymentMercadopagoQrDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentIntegrationImplTest {

    private PaymentIntegrationImpl paymentIntegration;
    private IntegrationConfig integrationConfig;

    @BeforeEach
    void setUp() {
        integrationConfig = mock(IntegrationConfig.class);
        paymentIntegration = new PaymentIntegrationImpl(integrationConfig);
    }

    @Test
    void deveCriarPagamentoComSucesso() {
        when(integrationConfig.getPaymentsUrl()).thenReturn("http://payment-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.toJson(any())).thenReturn("{}");
            mockedUtil.when(() -> IntegrationUtil.postForObject(anyString(), any())).then(invocation -> null);

            assertDoesNotThrow(this::execute);

            mockedUtil.verify(() -> IntegrationUtil.postForObject(eq("http://payment-service/charge"), any()));
        }
    }

    @Test
    void deveCancelarPagamentoPorCustomerOrderId() {
        when(integrationConfig.getPaymentsUrl()).thenReturn("http://payment-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            ResponseEntity<String> mockResponse = new ResponseEntity<>("Success", HttpStatus.OK);
            mockedUtil.when(() -> IntegrationUtil.patchForObject(anyString(), any())).thenReturn(mockResponse);

            assertDoesNotThrow(this::execute2);

            mockedUtil.verify(() -> IntegrationUtil.patchForObject(eq("http://payment-service/123/cancel"), isNull()));
        }
    }

    @Test
    void deveBuscarPagamentoPorCustomerOrderIdComSucesso() {
        when(integrationConfig.getPaymentsUrl()).thenReturn("http://payment-service");
        PaymentMercadopagoQrDTO expectedPayment = new PaymentMercadopagoQrDTO();
        expectedPayment.setOrderId(123);

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(PaymentMercadopagoQrDTO.class)))
                    .thenReturn(expectedPayment);

            PaymentMercadopagoQrDTO result = paymentIntegration.getPaymentByCustomerOrderId(123);

            assertNotNull(result);
            assertEquals(123, result.getOrderId());
            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://payment-service/customerOrder/123"), eq(PaymentMercadopagoQrDTO.class)));
        }
    }

    @Test
    void deveLancarExcecaoQuandoPagamentoNaoForEncontrado() {
        when(integrationConfig.getPaymentsUrl()).thenReturn("http://payment-service");

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(PaymentMercadopagoQrDTO.class)))
                    .thenThrow(new IntegrationException("Não encontrado registro para http://payment-service/customerOrder/999", 404));

            assertThrows(IntegrationException.class, this::execute3);

            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://payment-service/customerOrder/999"), eq(PaymentMercadopagoQrDTO.class)));
        }
    }

    @Test
    void deveAtualizarStatusKitchenOrderComSucesso() {
        when(integrationConfig.getPaymentsUrl()).thenReturn("http://payment-service");
        PaymentMercadopagoQrDTO expectedPayment = new PaymentMercadopagoQrDTO();
        expectedPayment.setOrderId(123);

        try (MockedStatic<IntegrationUtil> mockedUtil = mockStatic(IntegrationUtil.class)) {
            mockedUtil.when(() -> IntegrationUtil.getForObject(anyString(), eq(PaymentMercadopagoQrDTO.class)))
                    .thenReturn(expectedPayment);

            assertDoesNotThrow(() -> {
                PaymentMercadopagoQrDTO result = paymentIntegration.getPaymentByOrderId(123);
                assertNotNull(result);
                assertEquals(123, result.getOrderId());
            });

            mockedUtil.verify(() -> IntegrationUtil.getForObject(eq("http://payment-service/customerOrder/123"), eq(PaymentMercadopagoQrDTO.class)));
        }
    }

    private void execute() {
        paymentIntegration.createPayment(123, 50.0);
    }

    private void execute2() {
        paymentIntegration.cancelPaymentChargeByCustomerOrderId(123);
    }

    private void execute3() {
        paymentIntegration.getPaymentByOrderId(999);
    }
}
