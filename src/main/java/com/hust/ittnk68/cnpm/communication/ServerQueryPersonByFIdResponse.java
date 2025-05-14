package com.hust.ittnk68.cnpm.communication;

import java.util.List;
import java.util.Map;

import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class ServerQueryPersonByFIdResponse extends ServerResponseBase {

	private List< Map<String, Object> > list;

	public ServerQueryPersonByFIdResponse(ResponseStatus responseStatus, String responseMessage,
			List<Map<String, Object>> list) {
		super(responseStatus, responseMessage);
		this.list = list;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
    
}
