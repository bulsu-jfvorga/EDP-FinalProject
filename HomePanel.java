package agapay;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.Group;

public class HomePanel extends VBox {

    private static final String PRIMARY = "#800000";
    private static final String ACCENT = "#D4AF37";
    private static final String BG = "#F5F6FA";
    private static final String CARD = "#FFFFFF";

    public HomePanel() {

        VBox container = new VBox(0);
        container.setAlignment(Pos.TOP_CENTER);
        container.setStyle("-fx-background-color: " + BG + ";");

        // HERO SECTION
        StackPane hero = new StackPane();
        hero.setPrefHeight(450);

        ImageView bgImg = new ImageView();

        try {
            bgImg.setImage(new Image(getClass().getResourceAsStream("/images/bulsu.jpeg")));
        } catch (Exception e) {
            System.out.println("Hero image not found.");
        }

        bgImg.setFitWidth(1100);
        bgImg.setFitHeight(450);
        bgImg.setPreserveRatio(false);

        VBox overlay = new VBox(15);
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(128,0,0,0.5), rgba(40,0,0,0.9));"
        );

        Text title = new Text("BULACAN STATE UNIVERSITY");
        title.setFill(Color.web(ACCENT));
        title.setFont(Font.font("System", FontWeight.BLACK, 45));

        Text subTitle = new Text("SDRRM: Agapay System");
        subTitle.setFill(Color.WHITE);
        subTitle.setFont(Font.font("System", FontWeight.BOLD, 22));

        overlay.getChildren().addAll(title, subTitle);
        hero.getChildren().addAll(bgImg, overlay);

        // CONTENT BODY
        VBox contentBody = new VBox(40);
        contentBody.setPadding(new Insets(40, 50, 50, 50));
        contentBody.setAlignment(Pos.TOP_CENTER);
        contentBody.setMaxWidth(1100);

        VBox welcomeSection = new VBox(10);
        welcomeSection.setAlignment(Pos.CENTER);

        Label schoolLbl = new Label("Institutional Profile");
        schoolLbl.setStyle(
            "-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";"
        );

        Rectangle goldLine = new Rectangle(100, 4, Color.web(ACCENT));
        welcomeSection.getChildren().addAll(schoolLbl, goldLine);

        HBox cards = new HBox(30);
        cards.setAlignment(Pos.CENTER);

        cards.getChildren().addAll(
            createCard(
                "School Profile",
                "Bulacan State University is a progressive institution of higher learning...",
                "info"
            ),
            createCard(
                "SDRRM Office",
                "The Strategic Disaster Risk Reduction Management (SDRRM) Office...",
                "shield"
            )
        );

    
// MAP SECTION (Interactive Image with + and - Zoom Buttons)
VBox mapSection = new VBox(15);
mapSection.setAlignment(Pos.CENTER);

Label mapLabel = new Label("Campus Location & Proximity");
mapLabel.setStyle(
    "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";"
);

VBox mapCard = new VBox(10);
mapCard.setAlignment(Pos.CENTER);
mapCard.setPrefSize(930, 500);
mapCard.setMaxSize(930, 500);

mapCard.setStyle(
    "-fx-background-color: " + CARD +
    "; -fx-padding: 10;" +
    "-fx-background-radius: 15;" +
    "-fx-border-radius: 15;" +
    "-fx-border-color: #DDDDDD;" +
    "-fx-border-width: 1;" +
    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.10), 10, 0, 0, 5);"
);


ImageView mapImage = new ImageView(
    new Image(getClass().getResourceAsStream("/images/map.png"))
);

mapImage.setPreserveRatio(true);
mapImage.setFitWidth(914);
mapImage.setFitHeight(434);

Group zoomGroup = new Group(mapImage);

StackPane imagePane = new StackPane(zoomGroup);
imagePane.setPrefSize(914, 434);
imagePane.setMaxSize(914, 434);

imagePane.setStyle(
    "-fx-background-color: #f5f5f5;" +
    "-fx-background-radius: 12;"
);

// Clip image inside border
Rectangle clip = new Rectangle(914, 434);
clip.setArcWidth(20);
clip.setArcHeight(20);
imagePane.setClip(clip);

// Zoom value tracker
final double[] scale = {1.0};

// Zoom In Button
Button zoomIn = new Button("+");
zoomIn.setStyle(
    "-fx-font-size: 18px;" +
    "-fx-font-weight: bold;" +
    "-fx-background-color: " + PRIMARY + ";" +
    "-fx-text-fill: white;" +
    "-fx-background-radius: 8;"
);

zoomIn.setOnAction(e -> {
    scale[0] *= 1.1;
    zoomGroup.setScaleX(scale[0]);
    zoomGroup.setScaleY(scale[0]);
});

// Zoom Out Button
Button zoomOut = new Button("-");
zoomOut.setStyle(
    "-fx-font-size: 18px;" +
    "-fx-font-weight: bold;" +
    "-fx-background-color: " + PRIMARY + ";" +
    "-fx-text-fill: white;" +
    "-fx-background-radius: 8;"
);

zoomOut.setOnAction(e -> {
    scale[0] *= 0.9;
    zoomGroup.setScaleX(scale[0]);
    zoomGroup.setScaleY(scale[0]);
});

// Buttons row
HBox zoomControls = new HBox(10, zoomOut, zoomIn);
zoomControls.setAlignment(Pos.CENTER);

final double[] mouse = new double[2];

imagePane.setOnMousePressed(e -> {
    mouse[0] = e.getSceneX();
    mouse[1] = e.getSceneY();
});

imagePane.setOnMouseDragged(e -> {
    double dx = e.getSceneX() - mouse[0];
    double dy = e.getSceneY() - mouse[1];

    zoomGroup.setTranslateX(zoomGroup.getTranslateX() + dx);
    zoomGroup.setTranslateY(zoomGroup.getTranslateY() + dy);

    mouse[0] = e.getSceneX();
    mouse[1] = e.getSceneY();
});

mapCard.getChildren().addAll(imagePane, zoomControls);
mapSection.getChildren().addAll(mapLabel, mapCard);

        // LATEST NEWS SECTION
        VBox newsSection = new VBox(20);
        newsSection.setAlignment(Pos.CENTER);

        Label sectionHeader = new Label("Latest News & Updates");
        sectionHeader.setStyle(
            "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";"
        );

        HBox horizontalBox = new HBox(20);
        horizontalBox.setAlignment(Pos.CENTER);

        try (Connection conn = DBConnection.getConnection()) {

            String sql =
                "SELECT TOP 3 image_path, caption, type FROM posts ORDER BY created_at DESC";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                String caption = rs.getString("caption");

                String teaser =
                    (caption != null && caption.length() > 80)
                        ? caption.substring(0, 80) + "..."
                        : caption;

                horizontalBox.getChildren().add(
                    createPostLayout(
                        rs.getString("image_path"),
                        rs.getString("type"),
                        teaser
                    )
                );
            }

        } catch (Exception e) {
            System.err.println("News Load Error: " + e.getMessage());
        }

        newsSection.getChildren().addAll(sectionHeader, horizontalBox);

        // ADD ALL SECTIONS
        contentBody.getChildren().addAll(
            welcomeSection,
            cards,
            mapSection,
            newsSection
        );

        container.getChildren().addAll(hero, contentBody);

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: " + BG + ";");

        this.getChildren().clear();
        this.getChildren().add(sp);

        VBox.setVgrow(sp, Priority.ALWAYS);
    }

    private VBox createPostLayout(String imagePath, String type, String caption) {

        VBox post = new VBox(0);
        post.setPrefWidth(320);

        post.setStyle(
            "-fx-background-color: " + CARD +
            "; -fx-background-radius: 15;" +
            "-fx-border-color: " + ACCENT +
            "; -fx-border-radius: 15;" +
            "-fx-border-width: 0.5;"
        );

        ImageView iv = new ImageView();
        iv.setFitWidth(320);
        iv.setFitHeight(180);

        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);

            if (file.exists()) {
                iv.setImage(new Image(file.toURI().toString()));
            }
        }

        Label typeTag = new Label(type != null ? type.toUpperCase() : "UPDATE");
        typeTag.setStyle(
            "-fx-background-color: " + ACCENT +
            "; -fx-text-fill: " + PRIMARY +
            "; -fx-font-weight: bold;" +
            "-fx-padding: 5 12;"
        );

        Label cap = new Label(caption != null ? caption : "");
        cap.setWrapText(true);
        cap.setPadding(new Insets(10));

        post.getChildren().addAll(iv, typeTag, cap);

        return post;
    }

    private VBox createCard(String title, String text, String iconType) {

        VBox card = new VBox(12);
        card.setPadding(new Insets(25));
        card.setPrefWidth(450);

        card.setStyle(
            "-fx-background-color: " + CARD +
            "; -fx-background-radius: 10;" +
            "-fx-border-color: transparent transparent transparent " + ACCENT +
            "; -fx-border-width: 0 0 0 5;"
        );

        Label t = new Label(title);
        t.setStyle(
            "-fx-font-weight: bold;" +
            "-fx-font-size: 20px;" +
            "-fx-text-fill: " + PRIMARY + ";"
        );

        Label d = new Label(text);
        d.setWrapText(true);

        card.getChildren().addAll(t, d);

        return card;
    }
}