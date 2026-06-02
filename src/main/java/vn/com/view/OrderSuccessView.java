package vn.com.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

import vn.com.controller.OrderController;
import vn.com.model.Order;
import vn.com.model.OrderItem;

public class OrderSuccessView extends JFrame {

    private final Order order;
    private final OrderController controller;

    public OrderSuccessView(Order order, OrderController controller) {
        this.order = order;
        this.controller = controller;

        setTitle("Đặt Hàng Thành Công");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("ĐẶT HÀNG THÀNH CÔNG", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(40, 167, 69));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String createdAt = order.getCreatedAt() != null ? order.getCreatedAt().format(formatter) : "";

        infoPanel.add(new JLabel("Mã đơn hàng: #" + order.getId()));
        infoPanel.add(new JLabel("Phương thức thanh toán: " + order.getPaymentMethod()));
        infoPanel.add(new JLabel("Trạng thái thanh toán: " + order.getPaymentStatus()));
        infoPanel.add(new JLabel("Ngày tạo: " + createdAt));

        centerPanel.add(infoPanel, BorderLayout.NORTH);

        String[] columnNames = { "Tên Sản Phẩm", "Số Lượng" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                tableModel.addRow(new Object[] {
                        item.getProduct().getName(),
                        item.getQuantity()
                });
            }
        }

        JTable productTable = new JTable(tableModel);
        productTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(productTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        JButton backButton = new JButton("Quay lại đặt hàng");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(200, 40));

        backButton.addActionListener(e -> {
            dispose();
            controller.restartOrder();
        });

        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
