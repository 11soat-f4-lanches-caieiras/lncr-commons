package br.com.tp.lncr.commons.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lncr.integration")
public class IntegrationConfig {
    private String customersUrl;
    private String customerOrdersUrl;
    private String foodItemsUrl;
    private String kitchenOrdersUrl;
    private String paymentsUrl;
    private String notificationUrl;

    public String getCustomersUrl() {
        return customersUrl;
    }

    public void setCustomersUrl(String customersUrl) {
        this.customersUrl = customersUrl;
    }

    public String getCustomerOrdersUrl() {
        return customerOrdersUrl;
    }

    public void setCustomerOrdersUrl(String customerOrdersUrl) {
        this.customerOrdersUrl = customerOrdersUrl;
    }

    public String getFoodItemsUrl() {
        return foodItemsUrl;
    }

    public void setFoodItemsUrl(String foodItemsUrl) {
        this.foodItemsUrl = foodItemsUrl;
    }

    public String getKitchenOrdersUrl() {
        return kitchenOrdersUrl;
    }

    public void setKitchenOrdersUrl(String kitchenOrdersUrl) {
        this.kitchenOrdersUrl = kitchenOrdersUrl;
    }

    public String getPaymentsUrl() {
        return paymentsUrl;
    }

    public void setPaymentsUrl(String paymentsUrl) {
        this.paymentsUrl = paymentsUrl;
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }

    public void setNotificationUrl(String notificationUrl) {
        this.notificationUrl = notificationUrl;
    }
}
