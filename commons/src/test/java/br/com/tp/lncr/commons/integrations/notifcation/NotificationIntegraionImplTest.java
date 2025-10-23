package br.com.tp.lncr.commons.integrations.notifcation;

import br.com.tp.lncr.commons.config.IntegrationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NotificationIntegraionImplTest {

    private NotificationIntegraionImpl notificationIntegration;
    private IntegrationConfig integrationConfig;

    @BeforeEach
    void setUp() {
        integrationConfig = mock(IntegrationConfig.class);
        notificationIntegration = new NotificationIntegraionImpl(integrationConfig);
    }

    @Test
    void deveEnviarNotificacaoComSucesso() {
        when(integrationConfig.getNotificationUrl()).thenReturn("http://notification-service");

        assertDoesNotThrow(() -> notificationIntegration.sendNotification("ORDER_READY", 123, "Pedido pronto para retirada"));
    }

    @Test
    void deveEnviarNotificacaoComTipoNulo() {
        when(integrationConfig.getNotificationUrl()).thenReturn("http://notification-service");

        assertDoesNotThrow(() -> notificationIntegration.sendNotification(null, 123, "Mensagem de teste"));
    }

    @Test
    void deveEnviarNotificacaoComArtefactIdNulo() {
        when(integrationConfig.getNotificationUrl()).thenReturn("http://notification-service");

        assertDoesNotThrow(() -> notificationIntegration.sendNotification("ORDER_READY", null, "Mensagem de teste"));
    }

    @Test
    void deveEnviarNotificacaoComMensagemNula() {
        when(integrationConfig.getNotificationUrl()).thenReturn("http://notification-service");

        assertDoesNotThrow(() -> notificationIntegration.sendNotification("ORDER_READY", 123, null));
    }

    @Test
    void deveEnviarNotificacaoComTodosParametrosNulos() {
        when(integrationConfig.getNotificationUrl()).thenReturn("http://notification-service");

        assertDoesNotThrow(() -> notificationIntegration.sendNotification(null, null, null));
    }
}
