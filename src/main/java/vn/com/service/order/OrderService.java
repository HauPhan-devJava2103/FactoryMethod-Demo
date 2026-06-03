package vn.com.service.order;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.com.dto.PaymentResult;
import vn.com.model.Order;
import vn.com.model.OrderItem;
import vn.com.model.Product;
import vn.com.pattern.payment.IPaymentHandler;
import vn.com.pattern.payment.PaymentHandlerFactory;
import vn.com.repository.OrderRepository;
import vn.com.repository.ProductRepository;
import vn.com.utils.EPaymentMethod;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentHandlerFactory paymentFactory;

    @Override
    public String calculateTotalAmount(List<Product> products) {
        double total = products.stream().mapToDouble(p -> p.getPrice()).sum();

        return new DecimalFormat("#,###").format(total) + " VND";
    }

    @Override
    public Order processOrder(EPaymentMethod paymentMethod) {
        List<Product> products = productRepository.findAll();

        double total = products.stream().mapToDouble(p -> p.getPrice()).sum();

        Order order = Order.builder()
                .paymentMethod(paymentMethod)
                .totalAmount(total)
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderItem> items = new ArrayList<>();
        for (Product p : products) {
            OrderItem item = OrderItem.builder()
                    .product(p)
                    .quantity(p.getQuantity())
                    .order(order)
                    .build();
            items.add(item);
        }
        order.setOrderItems(items);
        orderRepository.save(order);

        IPaymentHandler handler = paymentFactory.getHandler(paymentMethod);
        PaymentResult result = handler.processPayment(order);
        orderRepository.updatePaymentStatus(order.getId(), result.getStatus());
        return order;

    }

}
