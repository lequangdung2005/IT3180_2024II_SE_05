package com.hust.ittnk68.cnpm.service;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import org.springframework.stereotype.Service;

@Service
public class QrCodeService
{

	public byte[] generateQrCodeImage (String text, int width, int height)
		throws Exception
	{
		Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
		hints.put (EncodeHintType.CHARACTER_SET, "UTF-8");

		BitMatrix bitMatrix = new MultiFormatWriter ().encode (
			text,
			BarcodeFormat.QR_CODE,
			width,
			height,
			hints
		);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream ();
		MatrixToImageWriter.writeToStream (bitMatrix, "PNG", outputStream);

		return outputStream.toByteArray ();
	}

}
