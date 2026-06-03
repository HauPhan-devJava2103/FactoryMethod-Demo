package vn.com;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import vn.com.controller.OrderController;
import vn.com.view.OrderPreparationView;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ApplicationContext context = SpringApplication.run(Main.class, args);

        OrderController controller = context.getBean(OrderController.class);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            OrderPreparationView view = new OrderPreparationView(controller);
            view.setVisible(true);
        });
    }
}