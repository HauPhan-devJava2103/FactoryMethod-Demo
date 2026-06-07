package vn.com.pattern.payment.creator;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.com.pattern.payment.IPaymentHandler;
import vn.com.pattern.payment.impl.CodPaymentHandler;
import vn.com.utils.EPaymentMethod;

@Component
@RequiredArgsConstructor
public class CodPaymentHandlerCreator extends PaymentHandlerCreator {

    private final CodPaymentHandler codPaymentHandler;

    @Override
    public EPaymentMethod getPaymentMethod() {
        return EPaymentMethod.COD;
    }

    @Override
    protected IPaymentHandler createPaymentHandler() {
        return codPaymentHandler;
    }
}
