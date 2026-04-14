package agapay;

import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UpdatesPanel extends VBox {

    private static final String PRIMARY = "#800000";
    private static final String ACCENT = "#D4AF37";
    private static final String BG = "#F5F6FA";
    private static final String CARD = "#FFFFFF";

    public UpdatesPanel(HostServices hostServices) {

        VBox container = new VBox(30);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.TOP_CENTER);
        container.setStyle("-fx-background-color: " + BG + ";");

        // --- Title Header (Preserved) ---
        HBox header = new HBox();
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: " + CARD + "; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-color: " + PRIMARY + "; " +
                        "-fx-border-width: 0 0 0 5; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        header.setMaxWidth(1000);

        Label title = new Label("External Agency Updates");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";");
        header.getChildren().add(title);

        // --- Grid Layout (FIXED) ---
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10, 0, 30, 0));
        grid.setMaxWidth(1000); // Para pantay sa header

        // --- FIX HERE: I-set ang pantay-pantay na columns ---
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33.33); // Hatiin sa 3 pantay na columns
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33.33);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33.33);
        
        grid.getColumnConstraints().addAll(col1, col2, col3);

        // DATA FETCHING
        List<UpdateLinks> agencyList = fetchAgenciesFromDB();

        if (agencyList.isEmpty()) {
            Label noData = new Label("No data found.");
            grid.add(noData, 1, 0); // Ilagay sa gitnang column (index 1)
        } else {
            int row = 0;
            int col = 0;
            for (UpdateLinks agency : agencyList) {
                VBox card = createAgencyCard(agency.getName(), agency.getImagePath(), agency.getWebsite(), hostServices);
                // I-set ang alignment ng mismong card sa loob ng grid cell
                GridPane.setHalignment(card, javafx.geometry.HPos.CENTER); 
                
                grid.add(card, col, row);
                col++;
                if (col > 2) { 
                    col = 0; row++;
                }
            }
        }

        container.getChildren().addAll(header, grid);

        ScrollPane scroll = new ScrollPane(container);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: " + BG + "; -fx-focus-color: transparent;");

        this.getChildren().add(scroll);
    }

    private List<UpdateLinks> fetchAgenciesFromDB() {
        List<UpdateLinks> list = new ArrayList<>();
        String query = "SELECT name, image_path, website FROM update_links";

        try (Connection conn = DBConnection.getConnection(); 
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (conn != null) {
                while (rs.next()) {
                    list.add(new UpdateLinks(
                        rs.getString("name"),
                        rs.getString("image_path"),
                        rs.getString("website")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }
        return list;
    }

    private VBox createAgencyCard(String name, String image_path, String website, HostServices hostServices) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefSize(220, 240);
        // Design preserved
        card.setStyle("-fx-background-color: " + CARD + "; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: " + ACCENT + "; " +
                        "-fx-border-width: 0 0 4 0; " + 
                        "-fx-border-radius: 0 0 20 20; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 15, 0, 0, 8);");

        ImageView logo = new ImageView();
        try {
            Image img = new Image(getClass().getResourceAsStream(image_path));
            logo.setImage(img);
        } catch (Exception e) {
            System.out.println("Image not found: " + image_path);
        }
        logo.setFitWidth(100);
        logo.setFitHeight(100);
        logo.setPreserveRatio(true);

        Label lblName = new Label(name);
        lblName.setStyle("-fx-font-weight: bold; -fx-text-fill: " + PRIMARY + "; -fx-font-size: 14px;");

        Button linkBtn = new Button("VISIT WEBSITE");
        linkBtn.setCursor(Cursor.HAND);
        linkBtn.setStyle("-fx-background-color: " + PRIMARY + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px; -fx-background-radius: 5; -fx-padding: 5 15 5 15;");

        linkBtn.setOnMouseEntered(e -> linkBtn.setStyle("-fx-background-color: " + ACCENT + "; -fx-text-fill: " + PRIMARY + "; -fx-font-weight: bold; -fx-font-size: 11px; -fx-background-radius: 5; -fx-padding: 5 15 5 15;"));
        linkBtn.setOnMouseExited(e -> linkBtn.setStyle("-fx-background-color: " + PRIMARY + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px; -fx-background-radius: 5; -fx-padding: 5 15 5 15;"));

        linkBtn.setOnAction(e -> {
            if (hostServices != null) hostServices.showDocument(website);
        });

        card.getChildren().addAll(logo, lblName, linkBtn);
        return card;
    }
}

// Model Class (Siguraduhing nasa agapay package)
class UpdateLinks {
    private String name;
    private String image_path;
    private String website;

    public UpdateLinks(String name, String image_path, String website) {
        this.name = name;
        this.image_path = image_path;
        this.website = website;
    }

    public String getName() { return name; }
    public String getImagePath() { return image_path; }
    public String getWebsite() { return website; }
}