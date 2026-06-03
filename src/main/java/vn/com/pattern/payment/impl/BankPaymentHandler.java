package vn.com.pattern.payment.impl;

import org.springframework.stereotype.Component;

import vn.com.dto.PaymentResult;
import vn.com.model.Order;
import vn.com.pattern.payment.IPaymentHandler;
import vn.com.utils.EPaymentMethod;
import vn.com.utils.EPaymentStatus;
import vn.com.utils.IpUtil;

@Component
public class BankPaymentHandler implements IPaymentHandler {
    private static final int SERVER_PORT = 8080;

    @Override
    public EPaymentMethod getPaymentMethod() {
        return EPaymentMethod.BANK;
    }

    @Override
    public PaymentResult processPayment(Order order) {
        order.setPaymentStatus(EPaymentStatus.PENDING);

        // Tạo payment URL cho QR code
        String ip = IpUtil.getLocalIpAddress();
        String paymentUrl = "http://" + ip + ":" + SERVER_PORT
                + "/payment/success?orderId=" + order.getId();

        return PaymentResult.builder()
                .orderId(order.getId())
                .status(EPaymentStatus.PENDING)
                .paymentUrl(paymentUrl)
                .message("Vui lòng quét mã QR để thanh toán")
                .build();
    }
}
