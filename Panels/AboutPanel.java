package agapay;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class AboutPanel extends VBox {

    private static final String PRIMARY = "#800000";
    private static final String ACCENT = "#D4AF37";
    private static final String BG = "#F5F6FA";

    public AboutPanel() {
        VBox container = new VBox(40);
        container.setPadding(new Insets(50));
        container.setAlignment(Pos.TOP_CENTER);
        container.setStyle("-fx-background-color: " + BG + ";");

        // --- Title Header ---
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Label title = new Label("ABOUT THE SYSTEM");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: black; -fx-text-fill: " + PRIMARY + ";");

        Rectangle line = new Rectangle(150, 5, Color.web(ACCENT));
        line.setArcWidth(5);
        line.setArcHeight(5);

        headerBox.getChildren().addAll(title, line);

        // --- Content Section ---
        HBox contentLayout = new HBox(40);
        contentLayout.setAlignment(Pos.CENTER);
        contentLayout.setMaxWidth(1000);

        // Welcome / About
        VBox leftCol = new VBox(20);
        leftCol.setPrefWidth(480);

        Label welcomeTitle = new Label("About the Agapay SDRRM System");
        welcomeTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";");
        welcomeTitle.setWrapText(true);

        Text welcomeText = new Text(
                "Welcome to the Agapay System, a premier digital platform specifically engineered for School Disaster Risk and Management (SDRRM). Our primary mission is to foster a proactive culture of safety and resilience within the Bulacan State University community. By providing students, faculty, and staff with essential, real-time tools and information, we aim to ensure that everyone is better equipped to prepare for, respond to, and recover from various types of disasters."
        );
        welcomeText.setWrappingWidth(450);
        welcomeText.setStyle("-fx-font-size: 15px; -fx-fill: #444444;");

        leftCol.getChildren().addAll(welcomeTitle, welcomeText);

        // Vertical Divider
        Separator verticalLine = new Separator();
        verticalLine.setOrientation(javafx.geometry.Orientation.VERTICAL);
        verticalLine.setPrefHeight(300);
        verticalLine.setStyle("-fx-background-color: #DDDDDD;");

        VBox rightCol = new VBox(20);
        rightCol.setPrefWidth(480);

        Label visionTitle = new Label("Our Vision for a Resilient Campus");
        visionTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";");
        visionTitle.setWrapText(true);

        Text visionText = new Text(
                "We are deeply committed to preventing and mitigating the physical and emotional damages caused by both natural and man-made disasters. Through this platform, we strive to raise awareness and empower our community with the knowledge and resources necessary to navigate crises with confidence. We believe that by working together and staying informed, we can significantly reduce the impact of disasters and build a more resilient campus for future generations."
        );
        visionText.setWrappingWidth(450);
        visionText.setStyle("-fx-font-size: 15px; -fx-fill: #444444;");

        rightCol.getChildren().addAll(visionTitle, visionText);

        contentLayout.getChildren().addAll(leftCol, verticalLine, rightCol);

        // --- Wrapping ---
        container.getChildren().addAll(headerBox, contentLayout);

        ScrollPane scroll = new ScrollPane(container);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: " + BG + ";");

        this.getChildren().add(scroll);
    }
}