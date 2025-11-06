package br.com.tp.lncr.commons.integrations.fooditem;

import br.com.tp.lncr.commons.utils.IntegrationUtil;
import br.com.tp.lncr.commons.config.IntegrationConfig;
import br.com.tp.lncr.commons.integrations.IntegrationMapper;
import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderFoodItemDTO;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FoodItemIntegrationImpl implements FoodItemIntegration {

    public final IntegrationConfig integrationConfig;
    public final IntegrationMapper integrationMapper;

    public FoodItemIntegrationImpl(IntegrationConfig integrationConfig, IntegrationMapper integrationMapper) {
        this.integrationConfig = integrationConfig;
        this.integrationMapper = integrationMapper;
    }

    @Override
    public List<CustomerOrderFoodItemDTO> getFoodItemDetailList(List<Integer> foodItemIdList) {
        String ids = foodItemIdList.stream().map(String::valueOf).collect(Collectors.joining(","));
        String url = integrationConfig.getFoodItemsUrl() + "/listIds/" + ids;
        List<FoodItemDTO> foodItemDTOList = IntegrationUtil.getForObject(url, new TypeReference<>() {
        });
        return foodItemDTOList.stream().map(integrationMapper::toCustomerOrderFoodItemDTO).toList();
    }

}
