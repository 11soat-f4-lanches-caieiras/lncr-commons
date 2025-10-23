package br.com.tp.lncr.commons.integrations.notifcation;

import br.com.tp.lncr.commons.utils.IntegrationUtil;
import br.com.tp.lncr.commons.config.IntegrationConfig;
import br.com.tp.lncr.core.dtos.notification.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationIntegraionImpl implements NotifcationIntegration {

    private static final Logger log = LoggerFactory.getLogger(NotificationIntegraionImpl.class);
    private final IntegrationConfig integrationConfig;

    public NotificationIntegraionImpl(IntegrationConfig integrationConfig) {
        this.integrationConfig = integrationConfig;
    }

    public void sendNotification(String notificationType, Integer ArtefactId, String message) {
        String notificationUrl = integrationConfig.getNotificationUrl();
        NotificationDTO notificationDTO = new NotificationDTO(null, notificationType, ArtefactId, message, null);
        String requestbody = IntegrationUtil.toJson(notificationDTO);
        log.info("Enviando Notificação: {}", notificationUrl);
        log.info("RequestBody: {}",requestbody);
        IntegrationUtil.postForObject(notificationUrl,notificationDTO);
    }

}
