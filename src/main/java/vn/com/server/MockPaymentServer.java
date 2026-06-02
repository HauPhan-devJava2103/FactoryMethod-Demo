package vn.com.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.function.Consumer;

public class MockPaymentServer {

    private HttpServer server;
    private int port = 8080;

    public MockPaymentServer(int preferredPort) {
        this.port = preferredPort;
    }

    public void start(Consumer<String> onPaymentSuccess) {
        try {
            server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
            server.createContext("/payment/success", new PaymentHandler(onPaymentSuccess));
            server.setExecutor(null);
            server.start();
            System.out.println("Mock Payment Server started on port " + port);
        } catch (IOException e) {
            System.err.println("Could not start Mock Payment Server on port " + port);
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Mock Payment Server stopped.");
        }
    }

    public int getPort() {
        return port;
    }

    static class PaymentHandler implements HttpHandler {
        private final Consumer<String> onSuccess;

        public PaymentHandler(Consumer<String> onSuccess) {
            this.onSuccess = onSuccess;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String orderId = "unknown";
            if (query != null && query.contains("orderId=")) {
                String[] parts = query.split("orderId=");
                if (parts.length > 1) {
                    orderId = parts[1].split("&")[0];
                }
            }

            // HTML response
            String response = "<html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                              "<title>Xác nhận thanh toán</title></head>" +
                              "<body style='text-align: center; font-family: Arial, sans-serif; padding-top: 50px;'>" +
                              "<h1 style='color: #28a745;'>Thanh toán thành công</h1>" +
                              "<p>Đơn hàng #" + orderId + " đã được thanh toán.</p>" +
                              "<p>Vui lòng xem tiếp trên màn hình máy tính.</p>" +
                              "</body></html>";
            
            byte[] responseBytes = response.getBytes("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }

            onSuccess.accept(orderId);
        }
    }
}
