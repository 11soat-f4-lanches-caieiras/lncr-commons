package br.com.tp.lncr.commons.integrations.customerorder;

public interface CustomerOrderIntegration {

    void updateCustomerOrderStatus(Integer customerOrderId, String newStatus);
}
