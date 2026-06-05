package vn.com.view.utils;

import javafx.animation.FadeTransition;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

public class WebViewLoadingHelper {

    public static void setupWebView(javafx.embed.swing.JFXPanel jfxPanel, WebView webView, String url, Runnable onSuccessCallback) {
        StackPane root = new StackPane();
        
        VBox loadingBox = new VBox(15);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setStyle("-fx-background-color: #ffffff;");
        
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setPrefSize(50, 50);
        
        Label loadingLabel = new Label("Loading application...");
        loadingLabel.setFont(Font.font("Segoe UI", 16));
        loadingLabel.setTextFill(Color.valueOf("#555555"));
        
        loadingBox.getChildren().addAll(spinner, loadingLabel);
        
        webView.setOpacity(0);
        
        root.getChildren().addAll(webView, loadingBox);
        jfxPanel.setScene(new Scene(root));
        
        WebEngine webEngine = webView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                if (onSuccessCallback != null) {
                    onSuccessCallback.run();
                }
                
                FadeTransition fadeOut = new FadeTransition(Duration.millis(400), loadingBox);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(e -> root.getChildren().remove(loadingBox));
                
                FadeTransition fadeIn = new FadeTransition(Duration.millis(400), webView);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                
                fadeOut.play();
                fadeIn.play();
            } else if (newState == Worker.State.FAILED) {
                loadingLabel.setText("Failed to load application.");
                loadingLabel.setTextFill(Color.RED);
                spinner.setVisible(false);
            }
        });
        
        webEngine.load(url);
    }
}
