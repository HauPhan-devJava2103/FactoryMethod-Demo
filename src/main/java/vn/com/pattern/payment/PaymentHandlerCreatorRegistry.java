package vn.com.pattern.payment;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import vn.com.dto.PaymentResult;
import vn.com.model.Order;
import vn.com.pattern.payment.creator.PaymentHandlerCreator;
import vn.com.utils.EPaymentMethod;

@Component
public class PaymentHandlerCreatorRegistry {

    private final Map<EPaymentMethod, PaymentHandlerCreator> creatorMap;

    // creatorMap = {COD: CodPaymentHandlerCreator, BANK: BankPaymentHandlerCreator}
    public PaymentHandlerCreatorRegistry(List<PaymentHandlerCreator> creators) {
        this.creatorMap = creators.stream()
                .collect(Collectors.toMap(PaymentHandlerCreator::getPaymentMethod, Function.identity()));
    }

    public PaymentResult processPayment(EPaymentMethod paymentMethod, Order order) {
        PaymentHandlerCreator creator = creatorMap.get(paymentMethod);
        if (creator == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy PaymentHandlerCreator cho phương thức thanh toán: " + paymentMethod);
        }
        return creator.processPayment(order);
    }

}
