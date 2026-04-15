package agapay;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminLogin {
    private Runnable loginListener;
    private CheckBox rememberMe;

    // BulSU Color Palette
    private static final String PRIMARY = "#800000"; // Maroon
    private static final String ACCENT = "#D4AF37";  // Gold
    private static final String BG_LIGHT = "#F5F6FA";

    public void setLoginListener(Runnable listener) { this.loginListener = listener; }

    public boolean isRememberMeChecked() {
        return rememberMe.isSelected();
    }

    public AdminLogin() {
        Stage stage = new Stage();
        stage.setTitle("Admin Login");

        // Main background
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: " + BG_LIGHT + ";");

        // --- LOGIN CARD ---
        VBox card = new VBox(20);
        card.setMaxWidth(340);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(0, 0, 30, 0)); // Bottom padding only
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                     "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 15, 0, 0, 8);");

        // Header Section (Maroon Top)
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(30, 20, 30, 20));
        header.setStyle("-fx-background-color: " + PRIMARY + "; -fx-background-radius: 15 15 0 0;");

        Label brandName = new Label("AGAPAY SYSTEM");
        brandName.setTextFill(Color.WHITE);
        brandName.setStyle("-fx-font-size: 22px; -fx-font-weight: 900; -fx-letter-spacing: 2px;");
        
        Label subHeader = new Label("Administrator Portal");
        subHeader.setTextFill(Color.web(ACCENT));
        subHeader.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        
        header.getChildren().addAll(brandName, subHeader);

        // Form Section
        VBox form = new VBox(15);
        form.setPadding(new Insets(20, 35, 10, 35));
        
        // Custom Styled Input Fields
        TextField usernameField = createStyledTextField("Username");
        PasswordField passwordField = createStyledPasswordField("Password");

        rememberMe = new CheckBox("Keep me logged in");
        rememberMe.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        Button loginBtn = new Button("LOG IN");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setCursor(javafx.scene.Cursor.HAND);
        loginBtn.setStyle("-fx-background-color: " + ACCENT + "; -fx-text-fill: " + PRIMARY + "; " +
                         "-fx-font-weight: 900; -fx-font-size: 14px; -fx-background-radius: 8; " +
                         "-fx-padding: 12;");

        // Hover effect for button
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-background-color: #C19A2E; -fx-text-fill: " + PRIMARY + "; -fx-font-weight: 900; -fx-font-size: 14px; -fx-background-radius: 8; -fx-padding: 12;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-background-color: " + ACCENT + "; -fx-text-fill: " + PRIMARY + "; -fx-font-weight: 900; -fx-font-size: 14px; -fx-background-radius: 8; -fx-padding: 12;"));

        Runnable loginAction = () -> {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND role = 'admin'";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, usernameField.getText());
                pst.setString(2, passwordField.getText());
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        if (loginListener != null) loginListener.run();
                        stage.close();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Credentials");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                    }
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        };

        loginBtn.setOnAction(e -> loginAction.run());
        
        form.getChildren().addAll(
            new Label("USERNAME") {{ setStyle("-fx-font-weight: bold; -fx-text-fill: " + PRIMARY + "; -fx-font-size: 11px;"); }},
            usernameField, 
            new Label("PASSWORD") {{ setStyle("-fx-font-weight: bold; -fx-text-fill: " + PRIMARY + "; -fx-font-size: 11px;"); }},
            passwordField, 
            rememberMe, 
            loginBtn
        );

        card.getChildren().addAll(header, form);
        root.getChildren().add(card);

        stage.setScene(new Scene(root, 450, 600));
        stage.show();
    }

    // Helper para sa magandang TextFields
    private TextField createStyledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: #F0F0F0; -fx-background-radius: 8; -fx-padding: 10; -fx-border-color: #E0E0E0; -fx-border-radius: 8;");
        return tf;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setStyle("-fx-background-color: #F0F0F0; -fx-background-radius: 8; -fx-padding: 10; -fx-border-color: #E0E0E0; -fx-border-radius: 8;");
        return pf;
    }
}