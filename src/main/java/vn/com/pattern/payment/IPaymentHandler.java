package vn.com.pattern.payment;

import vn.com.dto.PaymentResult;
import vn.com.model.Order;

public interface IPaymentHandler {

    // Xử lý thanh toán
    PaymentResult processPayment(Order order);

}
