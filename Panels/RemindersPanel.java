package agapay;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.*;

public class RemindersPanel {

    // BulSU Color Palette
    private static final String PRIMARY = "#800000"; // Maroon
    private static final String ACCENT = "#D4AF37";  // Gold
    private static final String BG = "#F5F6FA";      // Light Gray/Blue BG
    private static final String CARD = "#FFFFFF";    // White Card

    public static ScrollPane create() {
        VBox container = new VBox(40);
        container.setPadding(new Insets(50, 40, 50, 40));
        container.setAlignment(Pos.TOP_CENTER);
        container.setStyle("-fx-background-color: " + BG + ";");

        // --- TITLE HEADER ---
        VBox headerBox = new VBox(12);
        headerBox.setAlignment(Pos.CENTER);
        Label title = new Label("SAFETY REMINDERS & CAMPUS MAPS");
        title.setStyle("-fx-font-size: 34px; -fx-font-weight: 900; -fx-text-fill: " + PRIMARY + "; -fx-font-family: 'Segoe UI Semibold';");
        Rectangle goldLine = new Rectangle(180, 5, Color.web(ACCENT));
        headerBox.getChildren().addAll(title, goldLine);

        // --- MAIN CONTENT COLUMNS ---
        HBox columns = new HBox(50);
        columns.setAlignment(Pos.TOP_CENTER);
        columns.setMaxWidth(1150);

        VBox leftCol = new VBox(25);
        leftCol.setPrefWidth(550);
        HBox.setHgrow(leftCol, Priority.ALWAYS);

        Label reminderTitle = new Label("GENERAL SAFETY GUIDELINES");
        reminderTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";");

        VBox guidelineContainer = new VBox(15);
        fetchRemindersFromDB(guidelineContainer);
        leftCol.getChildren().addAll(reminderTitle, guidelineContainer);

  
        Separator vDiv = new Separator();
        vDiv.setOrientation(javafx.geometry.Orientation.VERTICAL);
        vDiv.setStyle("-fx-background-color: transparent; -fx-border-color: " + ACCENT + "; -fx-border-width: 0 1.5 0 0; -fx-opacity: 0.3;");

        VBox rightCol = new VBox(30);
        rightCol.setPrefWidth(500);
        rightCol.setAlignment(Pos.TOP_CENTER);
        HBox.setHgrow(rightCol, Priority.ALWAYS);

        Label mapTitle = new Label("EMERGENCY VISUAL GUIDES");
        mapTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";");

        rightCol.getChildren().addAll(
            mapTitle, 
            createMapActionCard("College of Nursing Ground Floor", "View room layouts and fire safety equipment locations.", "/images/ground_floor.jpg"),
            createMapActionCard("Campus Evacuation Strategy", "Official safe zones, assembly points, and exit routes.", "/images/evacuation_map.jpg")
        );

        columns.getChildren().addAll(leftCol, vDiv, rightCol);
        container.getChildren().addAll(headerBox, columns);

        ScrollPane scroll = new ScrollPane(container);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: " + BG + ";");

        return scroll;
    }

    private static HBox createGuidelineCard(String text) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: " + CARD + "; -fx-background-radius: 12; " +
                     "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.06), 10, 0, 0, 4); " +
                     "-fx-border-color: #E8E8E8; -fx-border-width: 1; -fx-border-radius: 12;");

        StackPane iconBox = new StackPane();
        Rectangle iconCircle = new Rectangle(32, 32);
        iconCircle.setArcWidth(32); iconCircle.setArcHeight(32);
        iconCircle.setFill(Color.web(PRIMARY));
        Label iconLabel = new Label("!"); 
        iconLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        iconBox.getChildren().addAll(iconCircle, iconLabel);

        Label content = new Label(text);
        content.setWrapText(true);
        content.setMaxWidth(420);
        content.setStyle("-fx-font-size: 14.5px; -fx-text-fill: #34495E; -fx-font-family: 'Segoe UI';");

        card.getChildren().addAll(iconBox, content);

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #FFFDF8; -fx-background-radius: 12; " +
                     "-fx-effect: dropshadow(three-pass-box, rgba(212,175,55,0.2), 15, 0, 0, 5); " +
                     "-fx-border-color: " + ACCENT + "; -fx-border-width: 1; -fx-border-radius: 12;"));
        
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: " + CARD + "; -fx-background-radius: 12; " +
                     "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.06), 10, 0, 0, 4); " +
                     "-fx-border-color: #E8E8E8; -fx-border-width: 1; -fx-border-radius: 12;"));

        return card;
    }

    private static VBox createMapActionCard(String title, String desc, String imagePath) {
        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: " + CARD + "; -fx-padding: 25; -fx-background-radius: 15; " +
                     "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 15, 0, 0, 5); " +
                     "-fx-border-color: " + ACCENT + "; -fx-border-width: 0 0 0 5;"); // Gold left accent line

        Label lblTitle = new Label(title.toUpperCase());
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";");
        
        Label lblDesc = new Label(desc);
        lblDesc.setWrapText(true);
        lblDesc.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 13px;");

        Button btnView = new Button("OPEN INTERACTIVE MAP");
        btnView.setCursor(javafx.scene.Cursor.HAND);
        btnView.setStyle("-fx-background-color: " + PRIMARY + "; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-padding: 10 25; -fx-background-radius: 5;");
        
        btnView.setOnAction(e -> openImageWindow(imagePath, title));

        card.getChildren().addAll(lblTitle, lblDesc, btnView);
        return card;
    }

    private static void openImageWindow(String imagePath, String title) {
        Stage stage = new Stage();
        stage.setTitle("Agapay System - " + title);
        stage.initModality(Modality.APPLICATION_MODAL);

        ImageView imageView = new ImageView();
        try {
            imageView.setImage(new Image(RemindersPanel.class.getResourceAsStream(imagePath)));
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(750); 
        } catch (Exception e) {
            System.err.println("Map Image not found at " + imagePath);
        }

        StackPane root = new StackPane(imageView);
        root.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 20;");
        
        Label closeLabel = new Label("CLICK ANYWHERE TO CLOSE");
        closeLabel.setStyle("-fx-text-fill: #95A5A6; -fx-font-size: 12px;");
        StackPane.setAlignment(closeLabel, Pos.BOTTOM_CENTER);

        root.getChildren().add(closeLabel);
        root.setOnMouseClicked(e -> stage.close());

        stage.setScene(new Scene(root));
        stage.show();
    }

    private static void fetchRemindersFromDB(VBox list) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT content FROM reminders")) {
            
            while (rs.next()) {
                String rawContent = rs.getString("content");
                if (rawContent != null) {
                    String[] points = rawContent.split(";");
                    for (String p : points) {
                        if (!p.trim().isEmpty()) {
                            list.getChildren().add(createGuidelineCard(p.trim()));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            list.getChildren().add(createGuidelineCard("Prioritize personal safety and remain calm during emergencies."));
            list.getChildren().add(createGuidelineCard("Always follow the directions of BulSU safety marshals."));
        }
    }
}