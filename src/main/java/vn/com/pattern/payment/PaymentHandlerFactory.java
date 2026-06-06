package vn.com.pattern.payment;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import vn.com.utils.EPaymentMethod;

@Component
public class PaymentHandlerFactory {

    private final Map<EPaymentMethod, IPaymentHandler> handlerMap;

    // handerMap = {COD: CodPaymentHandler, BANK: BankPaymentHandler}
    public PaymentHandlerFactory(List<IPaymentHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(IPaymentHandler::getPaymentMethod, Function.identity()));
    }

    @SuppressWarnings("unchecked")
    public IPaymentHandler getHandler(EPaymentMethod paymentMethod) {
        IPaymentHandler handler = handlerMap.get(paymentMethod);
        if (handler == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy PaymentHandler cho phương thức thanh toán: " + paymentMethod);
        }
        return handler;
    }

}
