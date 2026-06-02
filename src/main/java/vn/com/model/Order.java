package vn.com.model;

import lombok.Data;
import vn.com.utils.EPaymentMethod;
import vn.com.utils.EPaymentStatus;

import java.time.LocalDateTime;

@Data
public class Order {

    private Long id;
    private Double totalAmount;
    private EPaymentStatus paymentStatus;
    private EPaymentMethod paymentMethod;

    private LocalDateTime createdAt;


}
