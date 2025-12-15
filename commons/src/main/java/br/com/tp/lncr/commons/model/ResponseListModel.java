package br.com.tp.lncr.commons.model;

import java.util.List;

public class ResponseListModel<T> {
    private final ResponseMetadata response;
    private final List<T> content;

    public ResponseListModel(ResponseMetadata response, List<T> content) {
        this.response = response;
        this.content = content;
    }

    public ResponseListModel(List<T> content) {
        this.response = new ResponseMetadata();
        this.content = content;
    }

    public ResponseMetadata getResponse() {
        return response;
    }

    public List<T> getContent() {
        return content;
    }
}
