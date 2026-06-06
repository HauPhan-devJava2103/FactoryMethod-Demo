package vn.com.controller;

import java.awt.Color;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.com.model.Order;
import vn.com.model.Product;
import vn.com.server.MockPaymentServer;
import vn.com.service.order.IOrderService;
import vn.com.service.product.IProductService;
import vn.com.utils.EPaymentMethod;
import vn.com.utils.EPaymentStatus;
import vn.com.utils.IpUtil;
import vn.com.view.OrderPreparationView;
import vn.com.view.OrderSuccessView;
import vn.com.view.PaymentWaitingView;

@Component
@RequiredArgsConstructor
public class OrderController {
    private OrderPreparationView view;
    private final IOrderService orderService;
    private final IProductService productService;

    public void setView(OrderPreparationView view) {
        this.view = view;
    }

    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    public String calculateTotalAmount(List<Product> products) {
        return orderService.calculateTotalAmount(products);
    }

    public void processOrder(EPaymentMethod paymentMethod) {
        Order order = orderService.processOrder(paymentMethod);
        if (view != null)
            view.dispose();
        if (order.getPaymentStatus() == EPaymentStatus.PENDING) {
            showBankPaymentFlow(order);
        } else {
            new OrderSuccessView(order, this).setVisible(true);
        }
    }

    private void showBankPaymentFlow(Order order) {
        MockPaymentServer server = new MockPaymentServer(8080);
        String ip = IpUtil.getLocalIpAddress();
        String paymentUrl = "http://" + ip + ":" + server.getPort()
                + "/payment/success?orderId=" + order.getId();
        PaymentWaitingView waitingView = new PaymentWaitingView(order.getId(), paymentUrl);
        waitingView.setVisible(true);
        server.start(orderIdStr -> {
            SwingUtilities.invokeLater(() -> {
                waitingView.updateStatus("Đã thanh toán!", Color.GREEN);
                order.setPaymentStatus(EPaymentStatus.PAID);
                orderService.updatePaymentStatus(order.getId(), EPaymentStatus.PAID);
                Timer timer = new Timer(1500, e -> {
                    waitingView.dispose();
                    server.stop();
                    new OrderSuccessView(order, this).setVisible(true);
                });
                timer.setRepeats(false);
                timer.start();
            });
        });
    }

    public void restartOrder() {
        OrderPreparationView newView = new OrderPreparationView(this);
        newView.setVisible(true);
    }
}