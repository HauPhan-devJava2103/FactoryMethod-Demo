package vn.com.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Color;

import vn.com.model.Order;
import vn.com.model.OrderItem;
import vn.com.model.Product;
import vn.com.server.MockPaymentServer;
import vn.com.utils.EPaymentMethod;
import vn.com.utils.EPaymentStatus;
import vn.com.utils.IpUtil;
import vn.com.view.OrderPreparationView;
import vn.com.view.OrderSuccessView;
import vn.com.view.PaymentWaitingView;

public class OrderController {
    private OrderPreparationView view;

    public OrderController() {
    }

    public void setView(OrderPreparationView view) {
        this.view = view;
    }

    /**
     * Fetch order data
     */
    public List<Product> getMockOrderProducts() {
        // TODO: call service get order prepare data
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Sách Lập Trình Java Swing");
        p1.setPrice(150000.0);
        p1.setQuantity(1);

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("Bút Bi Xanh");
        p2.setPrice(5000.0);
        p2.setQuantity(5);

        Product p3 = new Product();
        p3.setId(3L);
        p3.setName("Sổ Tay Bìa Da");
        p3.setPrice(45000.0);
        p3.setQuantity(2);

        return Arrays.asList(p1, p2, p3);
    }

    /**
     * Calculate total amount from products
     */
    public String calculateTotalAmount(List<Product> products) {
        double total = 0;
        for (Product product : products) {
            total += product.getTotalAmount();
        }

        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
        return formatter.format(total) + " VND";
    }

    /**
     * Handle process checkout order
     * Request: EPaymentMethod
     * Response: Model Order (id, paymentStatus, paymentMethod, createdAt, orderItems)
     */
    public void processOrder(EPaymentMethod paymentMethod) {
        List<Product> products = getMockOrderProducts();

        EPaymentStatus paymentStatus;
        if (paymentMethod == EPaymentMethod.BANK)
            paymentStatus = EPaymentStatus.PENDING;
        else
            paymentStatus = EPaymentStatus.PAID;

        Order order = new Order();
        order.setId(1001L);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus(paymentStatus);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (Product p : products) {
            OrderItem item = new OrderItem();
            item.setId((long) (Math.random() * 10000));
            item.setProduct(p);
            item.setQuantity(p.getQuantity());
            item.setOrder(order);
            items.add(item);

            total += p.getTotalAmount();
        }

        order.setOrderItems(items);
        order.setTotalAmount(total);

        if (view != null) {
            view.dispose();
        }

        if (paymentMethod == EPaymentMethod.BANK) {
            MockPaymentServer server = new MockPaymentServer(8080);
            String ip = IpUtil.getLocalIpAddress();
            String paymentUrl = "http://" + ip + ":" + server.getPort() + "/payment/success?orderId=" + order.getId();

            PaymentWaitingView waitingView = new PaymentWaitingView(order.getId(), paymentUrl);
            waitingView.setVisible(true);

            server.start(orderIdStr -> {
                SwingUtilities.invokeLater(() -> {
                    waitingView.updateStatus("Đã thanh toán!", Color.GREEN);
                    order.setPaymentStatus(EPaymentStatus.PAID);

                    Timer timer = new Timer(1500, e -> {
                        waitingView.dispose();
                        server.stop();
                        OrderSuccessView successView = new OrderSuccessView(order, this);
                        successView.setVisible(true);
                    });
                    timer.setRepeats(false);
                    timer.start();
                });
            });

        } else {
            OrderSuccessView successView = new OrderSuccessView(order, this);
            successView.setVisible(true);
        }
    }

    public void restartOrder() {
        OrderPreparationView newView = new OrderPreparationView(this);
        newView.setVisible(true);
    }
}