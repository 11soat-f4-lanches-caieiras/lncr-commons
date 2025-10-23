package br.com.tp.lncr.commons.integrations.kitchenorder;

import br.com.tp.lncr.commons.utils.IntegrationUtil;
import br.com.tp.lncr.commons.config.IntegrationConfig;
import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderDTO;
import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderFoodItemDTO;
import br.com.tp.lncr.core.dtos.kitchenorder.KitchenOrderDTO;
import br.com.tp.lncr.core.dtos.kitchenorder.KitchenOrderFoodItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class KitchenOrderIntegrationImpl implements KitchenOrderIntegration {

    private static final Logger log = LoggerFactory.getLogger(KitchenOrderIntegrationImpl.class);
    private final IntegrationConfig integrationConfig;

    public KitchenOrderIntegrationImpl(IntegrationConfig integrationConfig) {
        this.integrationConfig = integrationConfig;
    }

    @Override
    public void cancelKitchenOrderById(Integer kitchenOrderOrderId) {
        updateKitchenOrderById(kitchenOrderOrderId, "Cancelled", true);
    }

    @Override
    public void createKitchenOrder(CustomerOrderDTO customerOrderDTO) {
        String kitchenOrderUrl = integrationConfig.getKitchenOrdersUrl();
        KitchenOrderDTO kitchenOrderDTO = toKitchenOrderDTO(customerOrderDTO);
        String requestbody = IntegrationUtil.toJson(kitchenOrderDTO);
        log.info("Criando pedido na cozinha:");
        log.info("Url: {}", kitchenOrderUrl);
        log.info("RequestBody: {}", requestbody);
        IntegrationUtil.postForObject(kitchenOrderUrl, kitchenOrderDTO);
    }

    @Override
    public KitchenOrderDTO getKitchenOrderByCustomerOrderId(Integer customerOrderId) {
        String url = integrationConfig.getKitchenOrdersUrl() + "/customerOrder/" + customerOrderId;
        return IntegrationUtil.getForObject(url, KitchenOrderDTO.class);
    }

    @Override
    public void updateKitchenOrderById(Integer kitchenOrderOrderId, String newStatus, Boolean forceUpdate) {
        String url = integrationConfig.getKitchenOrdersUrl() +"/" + kitchenOrderOrderId + "/updateStatus/" + newStatus + "?forceUpdate="+forceUpdate;
        log.info("Url: {}", url);
        ResponseEntity<String> response = IntegrationUtil.patchForObject(url, null);
        log.info("StatusCode {}",response.getStatusCode());
        log.info("Response: {}",response.getBody());
    }

    private KitchenOrderDTO toKitchenOrderDTO(CustomerOrderDTO customerOrderDTO) {
        KitchenOrderDTO kitchenOrderDTO = new KitchenOrderDTO();
        kitchenOrderDTO.setCustomerOrderId(customerOrderDTO.getId());
        kitchenOrderDTO.setFoodItems(customerOrderDTO.getFoodItems().stream()
                .map(this::toKitchenOrderFoodItemDTO)
                .toList());
        return kitchenOrderDTO;
    }

    private KitchenOrderFoodItemDTO toKitchenOrderFoodItemDTO(CustomerOrderFoodItemDTO customerOrderFoodItemDTO) {
        KitchenOrderFoodItemDTO kitchenOrderFoodItemDTO = new KitchenOrderFoodItemDTO();
        kitchenOrderFoodItemDTO.setName(customerOrderFoodItemDTO.getName());
        kitchenOrderFoodItemDTO.setDescription(customerOrderFoodItemDTO.getDescription());
        kitchenOrderFoodItemDTO.setNotes(customerOrderFoodItemDTO.getNotes());
        return kitchenOrderFoodItemDTO;
    }

}
