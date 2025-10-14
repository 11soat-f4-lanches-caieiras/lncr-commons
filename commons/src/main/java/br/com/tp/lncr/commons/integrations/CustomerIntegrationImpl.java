package br.com.tp.lncr.commons.integrations;

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
    public CustomerOrderCustomerDTO getCustomerDetails(Integer customerId) {
        String url = integrationConfig.getCustomersUrl()+ "/" + customerId;
        CustomerDTO customerDTO = IntegrationUtil.getForObject(url, CustomerDTO.class);
        return this.integrationMapper.toCustomerOrderCustomerDTO(customerDTO);
    }

    @Override
    public List<CustomerOrderCustomerDTO> getCustomerDetailsList(List<Integer> customerIdList) {
        String ids = customerIdList.stream().map(String::valueOf).collect(Collectors.joining(","));
        String url = integrationConfig.getCustomersListUrl() + "/" + ids;
        List<CustomerDTO> customerDTOList =  IntegrationUtil.getForObject(url, new TypeReference<List<CustomerDTO>>() {});
        return customerDTOList.stream().map(integrationMapper::toCustomerOrderCustomerDTO).toList();
    }
}
