package com.hust.ittnk68.cnpm.communication;

import java.util.List;
import java.util.Map;

import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class ServerResponseTemporaryStayAbsentRequest extends ServerResponseBase {

    private List<Map<String, Object>> result;

    public ServerResponseTemporaryStayAbsentRequest(ResponseStatus responseStatus, String responseMessage,
            List<Map<String, Object>> result) {
        super(responseStatus, responseMessage);
        this.result = result;
    }

    public List<Map<String, Object>> getResult() {
        return result;
    }

    public void setResult(List<Map<String, Object>> result) {
        this.result = result;
    }

}
