package vn.com.service.order;

import java.util.List;

import vn.com.dto.OrderPaymentResult;
import vn.com.model.Product;
import vn.com.utils.EPaymentMethod;
import vn.com.utils.EPaymentStatus;

public interface IOrderService {
    String calculateTotalAmount(List<Product> products);

    OrderPaymentResult processOrder(EPaymentMethod paymentMethod);

    void updatePaymentStatus(Long orderId, EPaymentStatus status);

}
