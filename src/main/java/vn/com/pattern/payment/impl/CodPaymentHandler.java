package vn.com.pattern.payment.impl;

import org.springframework.stereotype.Component;

import vn.com.dto.PaymentResult;
import vn.com.model.Order;
import vn.com.pattern.payment.IPaymentHandler;
import vn.com.utils.EPaymentMethod;
import vn.com.utils.EPaymentStatus;

@Component
public class CodPaymentHandler implements IPaymentHandler {

    @Override
    public EPaymentMethod getPaymentMethod() {
        return EPaymentMethod.COD;
    }

    @Override
    public PaymentResult processPayment(Order order) {
        order.setPaymentStatus(EPaymentStatus.PAID);
        return PaymentResult.builder()
                .orderId(order.getId())
                .status(EPaymentStatus.PAID)
                .message("Đặt hàng COD thành công!")
                .build();
    }
}
