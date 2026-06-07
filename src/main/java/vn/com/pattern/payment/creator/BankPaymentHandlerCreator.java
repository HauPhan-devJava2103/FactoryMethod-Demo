package vn.com.pattern.payment.creator;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.com.pattern.payment.IPaymentHandler;
import vn.com.pattern.payment.impl.BankPaymentHandler;
import vn.com.utils.EPaymentMethod;

@Component
@RequiredArgsConstructor
public class BankPaymentHandlerCreator extends PaymentHandlerCreator {

    private final BankPaymentHandler bankPaymentHandler;

    @Override
    public EPaymentMethod getPaymentMethod() {
        return EPaymentMethod.BANK;
    }

    @Override
    protected IPaymentHandler createPaymentHandler() {
        return bankPaymentHandler;
    }
}
