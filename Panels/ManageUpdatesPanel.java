package agapay;

import javafx.application.Platform;
import javafx.stage.Stage; 
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.sql.*;

public class ManageUpdatesPanel extends VBox {

    private final VBox listContainer;
    private final Runnable refreshHome;
    
    private static final String PRIMARY = "#800000"; 
    private static final String ACCENT = "#D4AF37"; 
    private static final String CARD_BG = "#FFFFFF";

    public ManageUpdatesPanel(Runnable refreshHome) {
        this.refreshHome = refreshHome;
        setSpacing(25);
        setPadding(new Insets(20, 30, 30, 30));
        setStyle("-fx-background-color: transparent;");

        // --- HEADER SECTION ---
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label lblActive = new Label("CONTENT MANAGEMENT");
        lblActive.setStyle("-fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: " + PRIMARY + ";");
        Label lblSub = new Label("Review, modify, or remove active system announcements.");
        lblSub.setStyle("-fx-font-size: 13px; -fx-text-fill: #7F8C8D;");
        titleBox.getChildren().addAll(lblActive, lblSub);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button btnRefresh = new Button("🔄 REFRESH DATA");
        btnRefresh.setStyle("-fx-background-color: white; -fx-border-color: #DDD; -fx-border-radius: 20; -fx-padding: 8 20; -fx-font-weight: bold;");
        btnRefresh.setOnAction(e -> refreshList());

        header.getChildren().addAll(titleBox, spacer, btnRefresh);

        listContainer = new VBox(15); 
        listContainer.setPadding(new Insets(10, 5, 10, 5));
        listContainer.setFillWidth(true); 
        
        ScrollPane scroll = new ScrollPane(listContainer);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        
        VBox.setVgrow(scroll, Priority.ALWAYS);
        getChildren().addAll(header, scroll);
        
        Platform.runLater(this::refreshList);
    }

    public void refreshList() {
        listContainer.getChildren().clear();
        
        String query = "SELECT post_id, type, caption, created_at FROM posts ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int count = 0;
            while (rs.next()) {
                count++;
                listContainer.getChildren().add(createUpdateCard(
                    rs.getInt("post_id"),      
                    rs.getString("type"),      
                    rs.getString("caption"),   
                    rs.getString("created_at") 
                ));
            }
            if (count == 0) showEmptyState();
        } catch (SQLException e) {
            showErrorState(e.getMessage());
        }
    }

    private VBox createUpdateCard(int id, String type, String caption, String date) {
        HBox card = new HBox(20);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 15; " +
                     "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 12, 0, 0, 4); " +
                     "-fx-border-color: #EFEFEF; -fx-border-width: 1; -fx-border-radius: 15;");

        VBox textInfo = new VBox(4);
       
        Label lblCaption = new Label(caption); 
        lblCaption.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        
        Label lblTypeAndDate = new Label(type.toUpperCase() + " | Published: " + date);
        lblTypeAndDate.setStyle("-fx-font-size: 11px; -fx-text-fill: #95A5A6; -fx-font-weight: bold;");
        
        textInfo.getChildren().addAll(lblCaption, lblTypeAndDate);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnEdit = new Button("EDIT");
        btnEdit.setStyle("-fx-text-fill: " + ACCENT + "; -fx-font-weight: bold;");
        btnEdit.setOnAction(e -> handleEdit(id, type, caption));

        Button btnDelete = new Button("DELETE");
        btnDelete.setStyle("-fx-text-fill: #E74C3C; -fx-font-weight: bold;");
        btnDelete.setOnAction(e -> handleDelete(id));

        HBox actions = new HBox(12, btnEdit, btnDelete);
        card.getChildren().addAll(textInfo, spacer, actions);

        return new VBox(card);
    }

    private void handleEdit(int id, String oldType, String oldCaption) {
        Stage editStage = new Stage();
        editStage.setTitle("Modify Post");
        
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        ComboBox<String> typeDropdown = new ComboBox<>();
        typeDropdown.getItems().addAll("Announcement", "Event");
        typeDropdown.setValue(oldType); 
        typeDropdown.setMaxWidth(Double.MAX_VALUE);

        TextArea captionIn = new TextArea(oldCaption);
        captionIn.setWrapText(true);

        Button saveBtn = new Button("UPDATE POST");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setStyle("-fx-background-color: " + PRIMARY + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
        
        saveBtn.setOnAction(e -> {
           
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("UPDATE posts SET type = ?, caption = ? WHERE post_id = ?")) {
                pstmt.setString(1, typeDropdown.getValue());
                pstmt.setString(2, captionIn.getText());
                pstmt.setInt(3, id);
                pstmt.executeUpdate();
                
                editStage.close();
                refreshList();
                if (refreshHome != null) refreshHome.run();
            } catch (SQLException ex) { ex.printStackTrace(); }
        });

        layout.getChildren().addAll(new Label("Select Category:"), typeDropdown, new Label("Edit Caption:"), captionIn, saveBtn);
        editStage.setScene(new Scene(layout, 450, 500));
        editStage.show();
    }

    private void handleDelete(int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete this post?");
        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.OK) {
          
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM posts WHERE post_id = ?")) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                refreshList();
                if (refreshHome != null) refreshHome.run();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private void showEmptyState() {
        Label lbl = new Label("No active posts found.");
        lbl.setStyle("-fx-text-fill: #BDC3C7; -fx-font-size: 16px; -fx-font-style: italic; -fx-padding: 50;");
        listContainer.getChildren().add(lbl);
    }

    private void showErrorState(String msg) {
        Label lbl = new Label("Database Error: " + msg);
        lbl.setStyle("-fx-text-fill: #E74C3C; -fx-font-weight: bold; -fx-padding: 20;");
        listContainer.getChildren().add(lbl);
    }
}