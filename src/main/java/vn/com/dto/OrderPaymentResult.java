package vn.com.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.model.Order;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPaymentResult {
    private Order order;
    private PaymentResult paymentResult;
}
