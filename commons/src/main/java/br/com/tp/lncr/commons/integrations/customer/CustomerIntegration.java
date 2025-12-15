package br.com.tp.lncr.commons.integrations.customer;

import br.com.tp.lncr.core.dtos.customer.CustomerDTO;
import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderCustomerDTO;

import java.util.List;

public interface CustomerIntegration {

    CustomerDTO getCustomerDetails(Integer customerId);

    CustomerDTO getCustomerDetailsByDocument(String documentNumber);

    List<CustomerOrderCustomerDTO> getCustomerDetailsList(List<Integer> customerIdList);
}
