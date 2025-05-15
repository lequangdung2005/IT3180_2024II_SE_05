package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.model.TemporaryStayAbsentRequest;

public class PostTemporaryStayAbsentRequest extends ClientMessageBase {

    private TemporaryStayAbsentRequest temporaryStayAbsentRequest;

    public TemporaryStayAbsentRequest getTemporaryStayAbsentRequest() {
        return temporaryStayAbsentRequest;
    }

    public void setTemporaryStayAbsentRequest(TemporaryStayAbsentRequest temporaryStayAbsentRequest) {
        this.temporaryStayAbsentRequest = temporaryStayAbsentRequest;
    }

    public PostTemporaryStayAbsentRequest(String username, TemporaryStayAbsentRequest temporaryStayAbsentRequest) {
        super(username);
        this.temporaryStayAbsentRequest = temporaryStayAbsentRequest;
    }
    
}
