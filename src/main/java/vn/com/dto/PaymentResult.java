package vn.com.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.utils.EPaymentStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResult {
    private Long orderId;
    private EPaymentStatus status;
    private String paymentUrl;
    private String message;
}