package vn.com.view;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class PaymentWaitingView extends JFrame {

    private final Long orderId;
    private final String paymentUrl;
    private JLabel statusLabel;

    public PaymentWaitingView(Long orderId, String paymentUrl) {
        this.orderId = orderId;
        this.paymentUrl = paymentUrl;

        setTitle("Chờ Thanh Toán");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("Thanh Toán Chuyển Khoản", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        JLabel qrLabel = new JLabel();
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            BufferedImage qrImage = generateQRCodeImage(paymentUrl, 250, 250);
            qrLabel.setIcon(new ImageIcon(qrImage));
        } catch (Exception e) {
            qrLabel.setText("Lỗi hiển thị QR Code");
            e.printStackTrace();
        }

        centerPanel.add(qrLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel orderLabel = new JLabel("Mã đơn hàng: #" + orderId, SwingConstants.CENTER);
        orderLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        statusLabel = new JLabel("Trạng thái: Đang chờ quét mã...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(Color.BLUE);

        infoPanel.add(orderLabel);
        infoPanel.add(statusLabel);
        centerPanel.add(infoPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
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
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("Trạng thái: " + status);
            statusLabel.setForeground(color);
        });
    }
}
