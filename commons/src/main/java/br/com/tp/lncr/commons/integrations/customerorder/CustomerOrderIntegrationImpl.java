package br.com.tp.lncr.commons.integrations.customerorder;


import br.com.tp.lncr.commons.utils.IntegrationUtil;
import br.com.tp.lncr.commons.config.IntegrationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class CustomerOrderIntegrationImpl implements CustomerOrderIntegration {

    private static final Logger log = LoggerFactory.getLogger(CustomerOrderIntegrationImpl.class);
    public final IntegrationConfig integrationConfig;

    public CustomerOrderIntegrationImpl(IntegrationConfig integrationConfig) {
        this.integrationConfig = integrationConfig;
    }

    @Override
    public void updateCustomerOrderStatus(Integer customerOrderId, String newStatus) {
        String url = getUrl(customerOrderId, newStatus);
        log.info("Atualizando status da CustomeOrder {}", url);
        ResponseEntity<String> response = IntegrationUtil.patchForObject(url, null);
        log.info("Resposta da atualização do status do pedido de cliente: {}", response);
    }

    private String getUrl(Integer customerOrderId, String newStatus) {
        return integrationConfig.getCustomerOrdersUrl() + "/" + customerOrderId + "/updateStatus/" + newStatus;
    }
}
