package agapay;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ContactsPanel {

    // BulSU Color Palette
    private static final String BULSU_MAROON = "#800000";
    private static final String BULSU_GOLD = "#D4AF37";
    private static final String BG_LIGHT = "#F5F5F5";
    private static final String WHITE = "#FFFFFF";
    private static final String ROW_ALT = "#FFF9E6"; // Light gold tint para sa alternate rows

    private ContactsPanel() {}

    public static ScrollPane create() {
        VBox container = new VBox(0);
        container.setPadding(new Insets(40));
        container.setAlignment(Pos.TOP_CENTER);
        container.setStyle("-fx-background-color: " + BG_LIGHT + ";");

        // Section Title
        Label mainTitle = new Label("EMERGENCY HOTLINES");
        mainTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + BULSU_MAROON + ";");
        
        Line accentLine = new Line(0, 0, 500, 0);
        accentLine.setStroke(Color.web(BULSU_GOLD));
        accentLine.setStrokeWidth(3);

        VBox titleArea = new VBox(10, mainTitle, accentLine);
        titleArea.setAlignment(Pos.CENTER);
        titleArea.setPadding(new Insets(0, 0, 35, 0));
        container.getChildren().add(titleArea);

        // Table Header
        container.getChildren().add(createTableHeader());

        // Table Body
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT agency_name, contact_info FROM contacts ORDER BY contact_id";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            boolean isAlternate = false;
            while (rs.next()) {
                String agency = rs.getString("agency_name");
                String info = rs.getString("contact_info");

                // Alternate between white and light gold
                String rowColor = isAlternate ? ROW_ALT : WHITE;
                container.getChildren().add(createTableRow(agency, info, rowColor));
                
                isAlternate = !isAlternate;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ScrollPane scroll = new ScrollPane(container);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: " + BG_LIGHT + ";");

        return scroll;
    }

    private static GridPane createTableHeader() {
        GridPane header = new GridPane();
        // Maroon background with Gold border for the title row
        header.setStyle("-fx-background-color: " + BULSU_MAROON + "; " +
                     "-fx-border-color: " + BULSU_GOLD + "; -fx-border-width: 2 2 1 2;");
        header.setPadding(new Insets(20, 25, 20, 25));
        header.setMaxWidth(900);

        setupColumns(header);

        Label lblAgency = new Label("AGENCY / OFFICE");
        lblAgency.setStyle("-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 18px;");

        Label lblContact = new Label("CONTACT NUMBER");
        lblContact.setStyle("-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 18px;");

        header.add(lblAgency, 0, 0);
        header.add(lblContact, 1, 0);

        return header;
    }

    private static GridPane createTableRow(String agency, String info, String bgColor) {
        GridPane row = new GridPane();
        row.setStyle("-fx-background-color: " + bgColor + "; " +
                     "-fx-border-color: " + BULSU_GOLD + "; -fx-border-width: 0 2 1 2;");
        row.setPadding(new Insets(15, 25, 15, 25));
        row.setMaxWidth(900);
        row.setAlignment(Pos.CENTER_LEFT);

        setupColumns(row);

        Label lblAgency = new Label(agency.toUpperCase());
        lblAgency.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333; -fx-font-size: 15px;");
        lblAgency.setWrapText(true);

        Label lblInfo = new Label(info);
        // Highlighted maroon text for numbers
        lblInfo.setStyle("-fx-font-weight: 900; -fx-text-fill: " + BULSU_MAROON + "; -fx-font-size: 18px;");
        lblInfo.setAlignment(Pos.CENTER_RIGHT);

        row.add(lblAgency, 0, 0);
        row.add(lblInfo, 1, 0);

        return row;
    }

    private static void setupColumns(GridPane gp) {
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(65);
        
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(35);
        col2.setHalignment(HPos.RIGHT);

        gp.getColumnConstraints().addAll(col1, col2);
    }
}