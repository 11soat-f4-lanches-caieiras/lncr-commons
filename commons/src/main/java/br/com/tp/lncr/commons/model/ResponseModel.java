package br.com.tp.lncr.commons.model;

public class ResponseModel<T> {
    private final ResponseMetadata response;
    private final T content;

    public ResponseModel(ResponseMetadata response, T content) {
        this.response = response;
        this.content = content;
    }

    public ResponseModel(T content) {
        this.response = new ResponseMetadata();
        this.content = content;
    }

    public ResponseMetadata getResponse() {
        return response;
    }

    public T getContent() {
        return content;
    }


}
