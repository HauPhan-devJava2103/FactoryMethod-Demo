package vn.com.view;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import vn.com.controller.OrderController;
import vn.com.model.Order;
import vn.com.model.OrderItem;

public class OrderSuccessView extends JFrame {

    private final Order order;
    private final OrderController controller;
    private WebEngine webEngine;
    private final JavaBridge javaBridge = new JavaBridge();

    public OrderSuccessView(Order order, OrderController controller) {
        this.order = order;
        this.controller = controller;

        setTitle("Đặt Hàng Thành Công");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JFXPanel jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            webEngine = webView.getEngine();

            String url = getClass().getResource("/pages/order-success.html").toExternalForm();
            vn.com.view.utils.WebViewLoadingHelper.setupWebView(jfxPanel, webView, url, () -> {
                netscape.javascript.JSObject window = (netscape.javascript.JSObject) webEngine.executeScript("window");
                window.setMember("javaBridge", javaBridge);
                loadOrderData();
            });
        });
    }

    private void loadOrderData() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String createdAt = order.getCreatedAt() != null ? order.getCreatedAt().format(formatter) : "";

            StringBuilder orderInfoJson = new StringBuilder("{");
            orderInfoJson.append("\"id\":\"").append(order.getId()).append("\",");
            orderInfoJson.append("\"paymentMethod\":\"")
                    .append(order.getPaymentMethod() != null ? order.getPaymentMethod().name() : "").append("\",");
            orderInfoJson.append("\"paymentStatus\":\"")
                    .append(order.getPaymentStatus() != null ? order.getPaymentStatus().name() : "").append("\",");
            orderInfoJson.append("\"createdAt\":\"").append(createdAt).append("\"");
            orderInfoJson.append("}");

            StringBuilder itemsJson = new StringBuilder("[");
            if (order.getOrderItems() != null) {
                List<OrderItem> items = order.getOrderItems();
                for (int i = 0; i < items.size(); i++) {
                    OrderItem item = items.get(i);
                    itemsJson.append("{")
                            .append("\"productName\":\"").append(escapeJson(item.getProduct().getName())).append("\",")
                            .append("\"quantity\":").append(item.getQuantity())
                            .append("}");
                    if (i < items.size() - 1)
                        itemsJson.append(",");
                }
            }
            itemsJson.append("]");

            Platform.runLater(() -> {
                webEngine.executeScript(String.format("initData('%s', '%s')",
                        escapeJsString(orderInfoJson.toString()), escapeJsString(itemsJson.toString())));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        public void restartOrder() {
            SwingUtilities.invokeLater(() -> {
                dispose();
                controller.restartOrder();
            });
        }
    }
}
