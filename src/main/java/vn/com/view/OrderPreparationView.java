package vn.com.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import vn.com.controller.OrderController;
import vn.com.model.Product;
import vn.com.utils.EPaymentMethod;

import java.awt.*;
import java.util.List;

public class OrderPreparationView extends JFrame {

    private JTable productTable;
    private JComboBox<EPaymentMethod> paymentMethodComboBox;
    private JButton placeOrderButton;
    private JLabel totalAmountLabel;
    private DefaultTableModel tableModel;

    private final OrderController controller;

    public OrderPreparationView(OrderController controller) {
        this.controller = controller;
        this.controller.setView(this);

        setTitle("Chuẩn Bị Đơn Hàng");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        loadOrderData();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("CHI TIẾT ĐƠN HÀNG", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = { "Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);
        productTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));

        totalAmountLabel = new JLabel("Tổng cộng: 0 VND");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalAmountLabel.setForeground(Color.RED);

        infoPanel.add(new JLabel("Phương thức thanh toán:"));
        EPaymentMethod[] paymentMethods = EPaymentMethod.values();
        paymentMethodComboBox = new JComboBox<>(paymentMethods);

        infoPanel.add(paymentMethodComboBox);
        infoPanel.add(totalAmountLabel);

        placeOrderButton = new JButton("ĐẶT HÀNG");
        placeOrderButton.setFont(new Font("Arial", Font.BOLD, 14));
        placeOrderButton.setBackground(new Color(40, 167, 69));
        placeOrderButton.setForeground(Color.WHITE);
        placeOrderButton.setPreferredSize(new Dimension(150, 40));

        bottomPanel.add(infoPanel, BorderLayout.CENTER);
        bottomPanel.add(placeOrderButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        setupListeners();
    }

    /**
     * Fetch data order prepare
     */
    private void loadOrderData() {
        List<Product> products = controller.getProducts();
        displayProducts(products);
    }

    public void displayProducts(List<Product> products) {
        tableModel.setRowCount(0);
        for (Product product : products) {
            Object[] row = {
                    "SP00" + product.getId(),
                    product.getName(),
                    product.getQuantity(),
                    product.getPrice(),
                    product.getTotalAmount()
            };
            tableModel.addRow(row);
        }

        String totalText = controller.calculateTotalAmount(products);
        totalAmountLabel.setText("Tổng cộng: " + totalText);
    }

    private void setupListeners() {
        placeOrderButton.addActionListener(e -> {
            EPaymentMethod selectedPayment = (EPaymentMethod) paymentMethodComboBox.getSelectedItem();

            controller.processOrder(selectedPayment);
        });
    }

    /**
     * Hook: show notification
     */
    public void showNotification(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}