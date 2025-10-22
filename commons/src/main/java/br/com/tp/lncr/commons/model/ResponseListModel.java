package br.com.tp.lncr.commons.model;

import br.com.tp.lncr.core.model.ResponseMetadata;

import java.util.List;

public class ResponseListModel<T> {
    private final ResponseMetadata _response;
    private final List<T> _content;

    public ResponseListModel(ResponseMetadata response, List<T> _content) {
        this._response = response;
        this._content = _content;
    }

    public ResponseListModel(List<T> _content) {
        this._response = new ResponseMetadata();
        this._content = _content;
    }

    public ResponseMetadata getResponse() {
        return _response;
    }

    public List<T> getContent() {
        return _content;
    }
}
