package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class ServerCheckBankingResponse extends ServerResponseBase {

    private boolean hasBanked;

    public ServerCheckBankingResponse(ResponseStatus responseStatus, String responseMessage, boolean hasBanked) {
        super(responseStatus, responseMessage);
        this.hasBanked = hasBanked;
    }

    public boolean isHasBanked() {
        return hasBanked;
    }

    public void setHasBanked(boolean hasBanked) {
        this.hasBanked = hasBanked;
    }

}
