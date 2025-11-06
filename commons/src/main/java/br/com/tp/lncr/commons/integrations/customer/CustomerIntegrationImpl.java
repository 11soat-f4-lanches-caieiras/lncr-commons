package br.com.tp.lncr.commons.integrations.customer;

import br.com.tp.lncr.commons.integrations.IntegrationMapper;
import br.com.tp.lncr.commons.utils.IntegrationUtil;
import br.com.tp.lncr.commons.config.IntegrationConfig;
import br.com.tp.lncr.core.dtos.customer.CustomerDTO;
import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderCustomerDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerIntegrationImpl implements CustomerIntegration {

    private final IntegrationConfig integrationConfig;
    private final IntegrationMapper integrationMapper;

    public CustomerIntegrationImpl(IntegrationConfig integrationConfig, IntegrationMapper integrationMapper) {
        this.integrationConfig = integrationConfig;
        this.integrationMapper = integrationMapper;
    }

    @Override
    public CustomerDTO getCustomerDetails(Integer customerId) {
        String url = integrationConfig.getCustomersUrl()+ "/" + customerId;
        return IntegrationUtil.getForObject(url, CustomerDTO.class);
    }

    @Override
    public CustomerDTO getCustomerDetailsByDocument(String documentNumber) {
        String url = integrationConfig.getCustomersUrl()+ "/documentNumber/" + documentNumber;
        return IntegrationUtil.getForObject(url, CustomerDTO.class);
    }


    @Override
    public List<CustomerOrderCustomerDTO> getCustomerDetailsList(List<Integer> customerIdList) {
        String ids = customerIdList.stream().map(String::valueOf).collect(Collectors.joining(","));
        String url = integrationConfig.getCustomersUrl() + "/listIds/" + ids;
        List<CustomerDTO> customerDTOList =  IntegrationUtil.getForObject(url, new TypeReference<>() {});
        return customerDTOList.stream().map(integrationMapper::toCustomerOrderCustomerDTO).toList();
    }
}
