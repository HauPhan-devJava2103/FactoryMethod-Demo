package vn.com;

import javax.swing.*;

import vn.com.controller.OrderController;
import vn.com.view.OrderPreparationView;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            OrderController controller = new OrderController();
            OrderPreparationView view = new OrderPreparationView(controller);

            view.setVisible(true);
        });
    }
}