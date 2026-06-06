package vn.com.view;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class PaymentWaitingView extends JFrame {

    private final Long orderId;
    private final String paymentUrl;
    private WebEngine webEngine;

    public PaymentWaitingView(Long orderId, String paymentUrl) {
        this.orderId = orderId;
        this.paymentUrl = paymentUrl;

        setTitle("Chờ Thanh Toán");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        JFXPanel jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            webEngine = webView.getEngine();

            String url = getClass().getResource("/pages/payment-waiting.html").toExternalForm();
            vn.com.view.utils.WebViewLoadingHelper.setupWebView(jfxPanel, webView, url, () -> {
                loadQrData();
            });
        });
    }

    private void loadQrData() {
        try {
            BufferedImage qrImage = generateQRCodeImage(paymentUrl, 250, 250);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            String base64Qr = Base64.getEncoder().encodeToString(baos.toByteArray());

            Platform.runLater(() -> {
                webEngine.executeScript(String.format("initData('%s', '%s')", orderId, base64Qr));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedImage generateQRCodeImage(String barcodeText, int width, int height) throws WriterException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, width, height, hints);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public void updateStatus(String status, Color color) {
        String hexColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        Platform.runLater(() -> {
            if (webEngine != null) {
                webEngine.executeScript(String.format("updateStatus('%s', '%s')", status, hexColor));
            }
        });
    }
}
