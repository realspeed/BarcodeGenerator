package com.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This class contains QR Code generation related functionality
 * @author Dhananjay Samanta
 *
 */
public class QRCodeCreatorUtil {
	
	private QRCodeCreatorUtil() {
		// No OP
	}

	/**
	 * This method has been written for create QRCode
	 * @param qrCodeContent
	 * @param qrCodeSize
	 * @param path
	 */
	public static void createQrCode(String qrCodeContent, int qrCodeSize, String path) {
		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, 250, 250,
					getEncodedHintType());
			int width = byteMatrix.getWidth();
			BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
			image.createGraphics();

			// set graphics of QR
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, width, width);
			graphics.setColor(Color.BLACK);

			IntStream.range(0, width).forEach(i -> {
				IntStream.range(0, width).forEach(j -> {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				});
			});

			createPDFwithQrImage(path + "/QR_Code_" + qrCodeContent + ".pdf", toByteArrayAutoClosable(image, "png"),
					qrCodeSize);

		} catch (Exception e) {
			System.out.println("QR Creation Failed!! :: "+e.getMessage());
		}
	}

	/**
	 * This method has been written for get static metadata for QR Code Creation
	 * @return
	 */
	private static Map<EncodeHintType, Object> getEncodedHintType() {
		Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
		hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hintMap.put(EncodeHintType.MARGIN, 1);
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		return hintMap;
	}

	/**
	 * This method has been written for convert java.awt.Image into byte array
	 * @param image
	 * @param type
	 * @return
	 * @throws IOException
	 */
	private static byte[] toByteArrayAutoClosable(BufferedImage image, String type) throws IOException {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			ImageIO.write(image, type, out);
			return out.toByteArray();
		}
	}

	
	/**
	 * This method has been written for create PDF file which contains QR Code
	 * @param pdfCreationFilepath
	 * @param imageInByteArray
	 * @param qrCodeSize
	 * @throws Exception
	 */
	private static void createPDFwithQrImage(String pdfCreationFilepath, byte[] imageInByteArray, int qrCodeSize) throws Exception {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(pdfCreationFilepath));
			document.open();
			Image img = Image.getInstance(imageInByteArray);
			img.scaleAbsolute(qrCodeSize, qrCodeSize);
			document.add(img);
			document.close();
	}
}
