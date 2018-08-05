package com.test;

import com.util.QRCodeCreatorUtil;

public class Test {
	public static void main(String[] args) {
		
		String qrCodeContent="www.google.com";
		int sizeOfQrCode=150;
		String qrCreationPath="E:/";
		
		QRCodeCreatorUtil.createQrCode(qrCodeContent,sizeOfQrCode, qrCreationPath);
		
	}
	
}
