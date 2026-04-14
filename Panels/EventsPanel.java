package agapay;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import agapay.DBConnection; 

public class EventsPanel extends VBox {

    private static final String PRIMARY = "#800000";
    private static final String ACCENT = "#D4AF37";
    private static final String BG = "#F5F6FA";
    private static final String CARD = "#FFFFFF";

    public EventsPanel() {
        VBox panelContent = createFilteredPostPanel("event", "Events Page");
        
        ScrollPane scroll = new ScrollPane(panelContent);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: " + BG + "; -fx-border-color: transparent;");
        
        this.getChildren().add(scroll);
    }

    private VBox createFilteredPostPanel(String filterType, String titleText) {
        VBox container = new VBox(35);
        container.setPadding(new Insets(40, 50, 40, 50));
        container.setAlignment(Pos.TOP_CENTER);
        container.setStyle("-fx-background-color: " + BG + ";");

        // --- TITLE SECTION ---
        VBox titleHeader = new VBox(10);
        titleHeader.setAlignment(Pos.CENTER);
        
        Label headTitle = new Label(titleText.toUpperCase());
        headTitle.setFont(Font.font("System", FontWeight.BLACK, 32));
        headTitle.setTextFill(Color.web(PRIMARY));
        
        Rectangle accentLine = new Rectangle(120, 5, Color.web(ACCENT));
        accentLine.setArcWidth(5);
        accentLine.setArcHeight(5);
        
        titleHeader.getChildren().addAll(headTitle, accentLine);

        // --- GRID FOR POSTS ---
        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(40);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setMaxWidth(1000); 

        int col = 0, row = 0;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT image_path, caption, created_at FROM posts WHERE type = ? ORDER BY created_at DESC";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, filterType);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                VBox postCard = new VBox(0); 
                postCard.setPrefWidth(460);
                postCard.setStyle("-fx-background-color: " + CARD + "; " +
                                 "-fx-background-radius: 15; " +
                                 "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 15, 0, 0, 10);");

                StackPane imgContainer = new StackPane();
                imgContainer.setPrefHeight(250);
                
                ImageView iv = new ImageView();
                iv.setFitWidth(460);
                iv.setFitHeight(250);
                iv.setPreserveRatio(false);

                Rectangle clip = new Rectangle(460, 250);
                clip.setArcWidth(30);
                clip.setArcHeight(30);
                iv.setClip(clip);

                String path = rs.getString("image_path");
                if (path != null && !path.isEmpty()) {
                    File file = new File(path);
                    if (file.exists()) {
                        iv.setImage(new Image(file.toURI().toString()));
                    }
                }
                imgContainer.getChildren().add(iv);

                VBox textContent = new VBox(15);
                textContent.setPadding(new Insets(20, 25, 20, 25));

                Label typeTag = new Label(filterType.toUpperCase());
                typeTag.setStyle("-fx-background-color: " + PRIMARY + "; -fx-text-fill: " + ACCENT + "; " +
                                "-fx-font-weight: bold; -fx-font-size: 11px; -fx-padding: 4 12; -fx-background-radius: 5;");

                Label cap = new Label(rs.getString("caption"));
                cap.setWrapText(true);
                cap.setMinHeight(60);
                cap.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
                cap.setTextFill(Color.web("#2C3E50"));

                Separator sep = new Separator();
                sep.setPadding(new Insets(5, 0, 5, 0));

                HBox footer = new HBox(8);
                footer.setAlignment(Pos.CENTER_LEFT);
                
                String dateStr = rs.getString("created_at");
                Label dateLbl = new Label("📅 Published: " + (dateStr != null ? dateStr.substring(0, 10) : "Recent"));
                dateLbl.setFont(Font.font("System", FontWeight.NORMAL, 13));
                dateLbl.setTextFill(Color.web("#7F8C8D"));
                
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                
                Label sourceLbl = new Label("SDRRM BulSU");
                sourceLbl.setStyle("-fx-text-fill: " + PRIMARY + "; -fx-font-weight: bold; -fx-font-size: 12px;");

                footer.getChildren().addAll(dateLbl, spacer, sourceLbl);
                textContent.getChildren().addAll(typeTag, cap, sep, footer);
                postCard.getChildren().addAll(imgContainer, textContent);
                
                postCard.setOnMouseEntered(e -> postCard.setStyle("-fx-background-color: " + CARD + "; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, " + ACCENT + ", 15, 0, 0, 5); -fx-cursor: hand;"));
                postCard.setOnMouseExited(e -> postCard.setStyle("-fx-background-color: " + CARD + "; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 15, 0, 0, 10);"));

                grid.add(postCard, col, row);
                col++;
                if (col == 2) { col = 0; row++; }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        container.getChildren().addAll(titleHeader, grid);
        return container;
    }
}