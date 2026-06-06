package vn.com.pattern.payment;

import vn.com.dto.PaymentResult;
import vn.com.model.Order;
import vn.com.utils.EPaymentMethod;

public interface IPaymentHandler {

    // Loại thanh toán Handler xử lý
    EPaymentMethod getPaymentMethod();

    // Xử lý thanh toán
    PaymentResult processPayment(Order order);

}
