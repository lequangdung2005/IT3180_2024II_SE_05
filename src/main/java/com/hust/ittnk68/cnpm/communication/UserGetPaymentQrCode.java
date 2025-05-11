package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.model.PaymentStatus;

public class UserGetPaymentQrCode extends ClientMessageBase {

    private PaymentRequest paymentRequest;
    private String serverUrl;
    private int width;
    private int height;

	public UserGetPaymentQrCode(String username, PaymentRequest paymentRequest, String serverUrl, int width, int height) {
		super(username);
        this.serverUrl = serverUrl;
		this.height = height;
		this.paymentRequest = paymentRequest;
		this.width = width;
	}

	public PaymentRequest getPaymentRequest() {
		return paymentRequest;
	}
	public void setPaymentRequest(PaymentRequest paymentRequest) {
		this.paymentRequest = paymentRequest;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

}
