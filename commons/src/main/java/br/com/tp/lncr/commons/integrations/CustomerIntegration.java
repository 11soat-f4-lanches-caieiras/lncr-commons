package br.com.tp.lncr.commons.integrations;

import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderCustomerDTO;

import java.util.List;

public interface CustomerIntegration {

    CustomerOrderCustomerDTO getCustomerDetails(Integer customerId);

    List<CustomerOrderCustomerDTO> getCustomerDetailsList(List<Integer> customerIdList);
}
