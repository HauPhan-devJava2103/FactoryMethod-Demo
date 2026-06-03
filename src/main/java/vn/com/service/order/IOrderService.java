package vn.com.service.order;

import java.util.List;

import vn.com.model.Order;
import vn.com.model.Product;
import vn.com.utils.EPaymentMethod;

public interface IOrderService {
    String calculateTotalAmount(List<Product> products);

    Order processOrder(EPaymentMethod paymentMethod);

}
