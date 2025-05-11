package com.hust.ittnk68.cnpm.communication;

import java.util.Base64;

import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class ServerQrCodeGenerateResponse extends ServerResponseBase {

    private String token;
    private String qrImageBase64;

	public ServerQrCodeGenerateResponse (ResponseStatus status, String message, String token, byte[] qrCodeData)
    {
        super (status, message);
        this.token = token;
        this.qrImageBase64 = (qrCodeData == null) ? "" : Base64.getEncoder ().encodeToString (qrCodeData);
    }

    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getQrImageBase64() {
		return qrImageBase64;
	}

	public void setQrImageBase64(String qrImageBase64) {
		this.qrImageBase64 = qrImageBase64;
	}
 
}
