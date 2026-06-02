package vn.com.model;

import lombok.Data;

@Data
public class OrderItem {
    private Long id;
    private int quantity;
    private Product product;
    private Order order;

}
