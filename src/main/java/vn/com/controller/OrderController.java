package vn.com.controller;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import vn.com.model.Product;
import vn.com.view.OrderPreparationView;

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
        // Sau này đoạn này sẽ đổi thành: return orderService.getCartItems();
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Sách Lập Trình Java Swing");
        p1.setPrice(150000.0);

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("Bút Bi Xanh");
        p2.setPrice(5000.0);

        Product p3 = new Product();
        p3.setId(3L);
        p3.setName("Sổ Tay Bìa Da");
        p3.setPrice(45000.0);

        return Arrays.asList(p1, p2, p3);
    }

    /**
     * Calculate total amount from products
     */
    public String calculateTotalAmount(List<Product> products) {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(total) + " VND";
    }

    /**
     * Handle process checkout order
     */
    public void processOrder(String paymentMethod) {
        // TODO
        String message = "Hệ thống đang xử lý đơn hàng...\nPhương thức thanh toán: " + paymentMethod;
        view.showNotification(message, "Thành Công", 1);
    }
}