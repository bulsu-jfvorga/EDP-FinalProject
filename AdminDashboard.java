package agapay;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class AdminDashboard extends BorderPane {
    
    private final StackPane contentArea;
    private static final String PRIMARY = "#800000"; // BulSU Maroon
    private static final String ACCENT = "#D4AF37";  // BulSU Gold
    private static final String SIDEBAR_BG = "#1A1A1A"; 

    public AdminDashboard(Runnable logoutAction, Runnable homeRefresh) {
        setStyle("-fx-background-color: #F8F9FA;");

        // --- SIDEBAR ---
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(30, 15, 30, 15));
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");

        Label brand = new Label("AGAPAY ADMIN");
        brand.setStyle("-fx-text-fill: " + ACCENT + "; -fx-font-size: 18px; -fx-font-weight: 900; -fx-padding: 0 0 30 10;");
        
        Button btnCreate = createNavButton("Create New Post", "📝");
        Button btnManage = createNavButton("Manage Updates", "⚙️");

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        Button logoutBtn = new Button("LOGOUT");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setCursor(javafx.scene.Cursor.HAND);
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #E74C3C; -fx-border-color: #E74C3C; -fx-border-radius: 5; -fx-font-weight: bold;");
        logoutBtn.setOnAction(e -> {
            AdminSession.setLoggedIn(false, false);
            logoutAction.run();
        });

        sidebar.getChildren().addAll(brand, btnCreate, btnManage, sidebarSpacer, logoutBtn);
        setLeft(sidebar);

        // --- MAIN WORKSPACE ---
        VBox mainWorkspace = new VBox();
        
        HBox header = new HBox();
        header.setPadding(new Insets(20, 40, 20, 40));
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        header.setAlignment(Pos.CENTER_LEFT);

        Label pageTitle = new Label("System Management");
        pageTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2D3436;");
        
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox profileBadge = new HBox(10);
        profileBadge.setAlignment(Pos.CENTER);
        Circle avatar = new Circle(15, Color.web(PRIMARY));
        Label adminRole = new Label("Authorized Personnel"); // Generic label
        adminRole.setStyle("-fx-font-weight: bold; -fx-text-fill: #636E72;");
        profileBadge.getChildren().addAll(adminRole, avatar);

        header.getChildren().addAll(pageTitle, headerSpacer, profileBadge);

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(30));
        
        CreatePostPanel createPanel = new CreatePostPanel(() -> homeRefresh.run());
        ManageUpdatesPanel managePanel = new ManageUpdatesPanel(homeRefresh);

        // Default view
        contentArea.getChildren().add(createPanel);

        btnCreate.setOnAction(e -> {
            contentArea.getChildren().setAll(createPanel);
            pageTitle.setText("Create Safety Announcement");
        });
        
        btnManage.setOnAction(e -> {
            contentArea.getChildren().setAll(managePanel);
            managePanel.refreshList();
            pageTitle.setText("Manage Active Updates");
        });

        mainWorkspace.getChildren().addAll(header, contentArea);
        setCenter(mainWorkspace);
    }

    private Button createNavButton(String text, String icon) {
        Button btn = new Button(icon + "  " + text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(12, 20, 12, 20));
        btn.setCursor(javafx.scene.Cursor.HAND);
        
        String normalStyle = "-fx-background-color: transparent; -fx-text-fill: #B2BEC3; -fx-font-size: 14px; -fx-font-weight: bold;";
        String hoverStyle = "-fx-background-color: #2D3436; -fx-text-fill: " + ACCENT + "; -fx-font-size: 14px; -fx-font-weight: bold;";
        
        btn.setStyle(normalStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> { if (!btn.isFocused()) btn.setStyle(normalStyle); });

        return btn;
    }
}