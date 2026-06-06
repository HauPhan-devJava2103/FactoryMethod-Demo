
CREATE DATABASE IF NOT EXISTS factory_method;
USE factory_method;

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_amount DOUBLE,
    payment_status VARCHAR(50),
    payment_method VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity INT NOT NULL,
    product_id BIGINT,
    order_id BIGINT,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

INSERT INTO products (id, name, price, quantity) VALUES
(1, 'Sách Lập Trình Java Swing', 150000.0, 1),
(2, 'Bút Bi Xanh', 5000.0, 5),
(3, 'Sổ Tay Bìa Da', 45000.0, 2),
(4, 'Chuột Logitech G102', 350000.0, 1),
(5, 'Bàn Phím Cơ TKL', 890000.0, 1);
