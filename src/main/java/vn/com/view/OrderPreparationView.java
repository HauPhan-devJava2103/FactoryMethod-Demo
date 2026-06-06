package vn.com.view;

import javax.swing.*;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import vn.com.controller.OrderController;
import vn.com.model.Product;
import vn.com.utils.EPaymentMethod;

import java.awt.*;
import java.util.List;

public class OrderPreparationView extends JFrame {

    private final OrderController controller;
    private WebEngine webEngine;

    private final JavaBridge javaBridge = new JavaBridge();

    public OrderPreparationView(OrderController controller) {
        this.controller = controller;
        this.controller.setView(this);

        setTitle("Chuẩn Bị Đơn Hàng");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JFXPanel jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);
        Platform.setImplicitExit(false);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            webEngine = webView.getEngine();

            String url = getClass().getResource("/pages/order-preparation.html").toExternalForm();
            vn.com.view.utils.WebViewLoadingHelper.setupWebView(jfxPanel, webView, url, () -> {
                netscape.javascript.JSObject window = (netscape.javascript.JSObject) webEngine.executeScript("window");
                window.setMember("javaBridge", javaBridge);
                loadOrderData();
            });
        });
    }

    private void loadOrderData() {
        List<Product> products = controller.getProducts();
        displayProducts(products);
    }

    public void displayProducts(List<Product> products) {
        try {
            StringBuilder productsJson = new StringBuilder("[");
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                productsJson.append("{")
                        .append("\"id\":").append(p.getId()).append(",")
                        .append("\"name\":\"").append(escapeJson(p.getName())).append("\",")
                        .append("\"quantity\":").append(p.getQuantity()).append(",")
                        .append("\"price\":").append(p.getPrice()).append(",")
                        .append("\"totalAmount\":").append(p.getTotalAmount())
                        .append("}");
                if (i < products.size() - 1)
                    productsJson.append(",");
            }
            productsJson.append("]");

            StringBuilder paymentMethodsJson = new StringBuilder("[");
            EPaymentMethod[] methods = EPaymentMethod.values();
            for (int i = 0; i < methods.length; i++) {
                paymentMethodsJson.append("{")
                        .append("\"value\":\"").append(methods[i].name()).append("\",")
                        .append("\"label\":\"").append(methods[i].name()).append("\"")
                        .append("}");
                if (i < methods.length - 1)
                    paymentMethodsJson.append(",");
            }
            paymentMethodsJson.append("]");

            String totalText = controller.calculateTotalAmount(products);

            Platform.runLater(() -> {
                webEngine.executeScript(String.format("initData('%s', '%s', '%s')",
                        escapeJsString(productsJson.toString()),
                        escapeJsString(paymentMethodsJson.toString()),
                        escapeJsString(totalText)));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNotification(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private String escapeJson(String text) {
        if (text == null)
            return "";
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String escapeJsString(String text) {
        if (text == null)
            return "";
        return text.replace("\\", "\\\\").replace("'", "\\'");
    }

    public class JavaBridge {
        public void processOrder(String paymentMethodStr) {
            SwingUtilities.invokeLater(() -> {
                try {
                    EPaymentMethod paymentMethod = EPaymentMethod.valueOf(paymentMethodStr);
                    controller.processOrder(paymentMethod);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
