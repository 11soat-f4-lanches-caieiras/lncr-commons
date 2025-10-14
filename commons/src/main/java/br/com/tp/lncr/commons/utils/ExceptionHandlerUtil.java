package br.com.tp.lncr.commons.utils;

import br.com.tp.lncr.core.model.ResponseMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ExceptionHandlerUtil {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerUtil.class);

    public static ResponseEntity<Object> handleException(String message, int code, Throwable throwable) {
        if (throwable != null) {
            log.error("Exceção lançada por: {}.{} - Mensagem: {}",
                    throwable.getStackTrace()[0].getClassName(),
                    throwable.getStackTrace()[0].getMethodName(),
                    message,
                    throwable);
        }
        return new ResponseEntity<>(createResponse(message), getHttpStatusByCode(code));
    }

    private static HttpStatus getHttpStatusByCode(int code) {
        return switch (code) {
            case 400 -> HttpStatus.BAD_REQUEST;
            case 401 -> HttpStatus.UNAUTHORIZED;
            case 403 -> HttpStatus.FORBIDDEN;
            case 404 -> HttpStatus.NOT_FOUND;
            case 409 -> HttpStatus.CONFLICT;
            case 500 -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    private static ResponseMetadata createResponse(String message) {
        return new ResponseMetadata(UUID.randomUUID().toString(), OffsetDateTime.now().toString(), message);
    }
}