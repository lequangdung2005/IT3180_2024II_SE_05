package com.hust.ittnk68.cnpm.callback;

import com.hust.ittnk68.cnpm.model.PaymentStatus;

public interface QrCodeGeneratorCallback {
	void run (PaymentStatus p, int a);
}
