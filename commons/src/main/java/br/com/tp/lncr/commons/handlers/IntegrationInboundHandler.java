package br.com.tp.lncr.commons.handlers;

import br.com.tp.lncr.commons.integrations.IntegrationException;
import br.com.tp.lncr.commons.utils.ExceptionHandlerUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class IntegrationInboundHandler {

    @ExceptionHandler(IntegrationException.class)
    public ResponseEntity<Object> handlerIntegrationException(IntegrationException ex) {
        return ExceptionHandlerUtil.handleException(ex.getMessage(), ex.getCode(), ex);
    }
}
