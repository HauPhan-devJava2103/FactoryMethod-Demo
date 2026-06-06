package vn.com.service.order;

import java.util.List;

import vn.com.model.Order;
import vn.com.model.Product;
import vn.com.utils.EPaymentMethod;
import vn.com.utils.EPaymentStatus;

public interface IOrderService {
    String calculateTotalAmount(List<Product> products);

    Order processOrder(EPaymentMethod paymentMethod);

    void updatePaymentStatus(Long orderId, EPaymentStatus status);

}
