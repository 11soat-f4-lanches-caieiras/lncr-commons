package br.com.tp.lncr.commons.integrations.kitchenorder;

import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderDTO;
import br.com.tp.lncr.core.dtos.kitchenorder.KitchenOrderDTO;

public interface KitchenOrderIntegration {

    void cancelKitchenOrderById(Integer kitchenOrderOrderId);

    void createKitchenOrder(CustomerOrderDTO customerOrderDTO);

    KitchenOrderDTO getKitchenOrderByCustomerOrderId(Integer id);

    void updateKitchenOrderById(Integer kitchenOrderOrderId, String newStatus, Boolean forceUpdate);
}
