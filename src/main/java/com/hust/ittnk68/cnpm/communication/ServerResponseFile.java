package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class ServerResponseFile extends ServerResponseBase {

    private String content;

	public ServerResponseFile(ResponseStatus responseStatus, String responseMessage, String content) {
		super(responseStatus, responseMessage);
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
}
