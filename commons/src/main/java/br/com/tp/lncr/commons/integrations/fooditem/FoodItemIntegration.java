package br.com.tp.lncr.commons.integrations.fooditem;

import br.com.tp.lncr.core.dtos.customerorder.CustomerOrderFoodItemDTO;

import java.util.List;

public interface FoodItemIntegration {

    List<CustomerOrderFoodItemDTO> getFoodItemDetailList(List<Integer> foodItemIdList);

}
