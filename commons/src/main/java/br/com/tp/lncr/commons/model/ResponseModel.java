package br.com.tp.lncr.commons.model;

import br.com.tp.lncr.core.model.ResponseMetadata;

public class ResponseModel<T> {
    private final ResponseMetadata _response;
    private final T _content;

    public ResponseModel(ResponseMetadata response, T content) {
        this._response = response;
        this._content = content;
    }

    public ResponseModel(T content) {
        this._response = new ResponseMetadata();
        this._content = content;
    }

    public ResponseMetadata getResponse() {
        return _response;
    }

    public T getContent() {
        return _content;
    }


}
