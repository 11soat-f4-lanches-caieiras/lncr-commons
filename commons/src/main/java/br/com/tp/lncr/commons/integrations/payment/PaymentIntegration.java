package br.com.tp.lncr.commons.integrations.payment;

import br.com.tp.lncr.core.dtos.payment.PaymentDTO;
import br.com.tp.lncr.core.dtos.payment.PaymentMercadopagoQrDTO;

public interface PaymentIntegration {

    void cancelPaymentChargeByCustomerOrderId(Integer customerOrderId);

    void createPayment(Integer customerOrderId, Double totalCost);

    PaymentMercadopagoQrDTO getPaymentByCustomerOrderId(Integer id);

    PaymentDTO getPaymentByOrderId(Integer customerOrderId);
}
