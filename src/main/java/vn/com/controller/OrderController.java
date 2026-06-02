package vn.com.controller;

import java.text.DecimalFormat;

import vn.com.view.OrderPreparationView;

public class OrderController {
    private OrderPreparationView view;

    public OrderController() {
        // Khởi tạo controller trống, view sẽ được gắn sau bằng setter
    }

    public void setView(OrderPreparationView view) {
        this.view = view;
    }

    /**
     * Fetch order data
     */
    public Object[][] getMockOrderProducts() {
        // Sau này đoạn này sẽ đổi thành: return orderService.getCartItems();
        return new Object[][] {
                { "SP001", "Sách Lập Trình Java Swing", 1, 150000, 150000 },
                { "SP002", "Bút Bi Xanh", 5, 5000, 25000 },
                { "SP003", "Sổ Tay Bìa Da", 2, 45000, 90000 }
        };
    }

    /**
     * Calculate total amount from products
     */
    public String calculateTotalAmount(Object[][] products) {
        double total = 0;
        for (Object[] row : products) {
            total += Double.parseDouble(row[4].toString());
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(total) + " VND";
    }

    /**
     * Xử lý logic khi người dùng nhấn nút Đặt hàng
     */
    public void processOrder(String paymentMethod) {
        // Xử lý logic nghiệp vụ đặt hàng tại đây (Validate, gửi xuống Service -> DAO để
        // lưu DB)
        // ...

        // Sau khi xử lý xong logic, điều khiển View hiển thị thông báo kết quả
        String message = "Hệ thống đang xử lý đơn hàng...\nPhương thức thanh toán: " + paymentMethod;
        view.showNotification(message, "Thành Công", 1); // 1 tương ứng với JOptionPane.INFORMATION_MESSAGE
    }
}