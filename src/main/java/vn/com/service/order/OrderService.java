package vn.com.service.order;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.com.dto.OrderPaymentResult;
import vn.com.dto.PaymentResult;
import vn.com.model.Order;
import vn.com.model.OrderItem;
import vn.com.model.Product;
import vn.com.pattern.payment.PaymentHandlerCreatorRegistry;
import vn.com.repository.OrderRepository;
import vn.com.repository.ProductRepository;
import vn.com.utils.EPaymentMethod;
import vn.com.utils.EPaymentStatus;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentHandlerCreatorRegistry paymentHandlerCreatorRegistry;

    @Override
    public String calculateTotalAmount(List<Product> products) {
        double total = products.stream().mapToDouble(p -> p.getPrice()).sum();

        return new DecimalFormat("#,###").format(total) + " VND";
    }

    /*
     * // Cách cũ - vi phạm OCP
     * public PaymentResult processPayment(Order order, EPaymentMethod method) {
     * if (method == EPaymentMethod.COD) {
     * // xử lý COD...
     * } else if (method == EPaymentMethod.BANK) {
     * // xử lý Bank...
     * }
     * // Thêm MOMO
     * // phải sửa method này
     * }
     */

    @Override
    public OrderPaymentResult processOrder(EPaymentMethod paymentMethod) {
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

        PaymentResult result = paymentHandlerCreatorRegistry.processPayment(paymentMethod, order);
        orderRepository.updatePaymentStatus(order.getId(), result.getStatus());
        return OrderPaymentResult.builder()
                .order(order)
                .paymentResult(result)
                .build();

    }

    @Override
    public void updatePaymentStatus(Long orderId, EPaymentStatus status) {
        orderRepository.updatePaymentStatus(orderId, status);
    }

}
