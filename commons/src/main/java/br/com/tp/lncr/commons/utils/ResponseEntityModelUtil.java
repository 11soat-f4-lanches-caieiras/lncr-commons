package br.com.tp.lncr.commons.utils;

import br.com.tp.lncr.commons.model.ResponseListModel;
import br.com.tp.lncr.commons.model.ResponseModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseEntityModelUtil {

    public static <T> ResponseEntity<ResponseModel<T>> response(T body, HttpStatus httpStatus, HttpHeaders httpHeaders) {
        return ResponseEntity.status(httpStatus)
                .headers(httpHeaders)
                .body(new ResponseModel<>(body));
    }

    public static <T> ResponseEntity<ResponseModel<T>> OK(T body) {
        return response(body, HttpStatus.OK, null);
    }

    public static <T> ResponseEntity<ResponseModel<T>> Accepted(T body) {
        return response(body, HttpStatus.ACCEPTED, null);
    }

    public static <T> ResponseEntity<ResponseModel<T>> created(T body, String location) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", location);
        return ResponseEntityModelUtil.response(body, HttpStatus.CREATED, headers);
    }

    public static <T> ResponseEntity<ResponseListModel<T>> listOK(List<T> body) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseListModel<>(body));
    }

    public static <T> ResponseEntity<ResponseModel<T>>  badRequest(T body) {
        return response(body, HttpStatus.BAD_REQUEST, null);
    }

}
