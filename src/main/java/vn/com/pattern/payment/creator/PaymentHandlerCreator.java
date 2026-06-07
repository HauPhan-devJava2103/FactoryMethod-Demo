package vn.com.pattern.payment.creator;

import vn.com.dto.PaymentResult;
import vn.com.model.Order;
import vn.com.pattern.payment.IPaymentHandler;
import vn.com.utils.EPaymentMethod;

public abstract class PaymentHandlerCreator {

    public abstract EPaymentMethod getPaymentMethod();

    protected abstract IPaymentHandler createPaymentHandler();

    public PaymentResult processPayment(Order order) {
        IPaymentHandler handler = createPaymentHandler();
        return handler.processPayment(order);
    }
}
