package agapay;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Agapay extends Application {

    private BorderPane root;
    private static final String PRIMARY = "#800000";
    private static final String ACCENT = "#D4AF37";
    private static final String BG = "#F5F6FA";

    private Rectangle underline;
    private Button homeBtn; 

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");

        // --- NAVBAR ---
        HBox menuBar = new HBox();
        menuBar.setPadding(new Insets(15, 30, 10, 30));
        menuBar.setAlignment(Pos.CENTER_LEFT);
        menuBar.setSpacing(30);

        homeBtn = new Button("Home");
        Button aboutBtn = new Button("About");
        Button contactsBtn = new Button("Contacts");
        Button announcementsBtn = new Button("Announcements");

        MenuButton dropdown = new MenuButton("More");
        MenuItem eventsItem = new MenuItem("Events");
        MenuItem updatesItem = new MenuItem("Updates");
        MenuItem remindersItem = new MenuItem("Reminders");
        dropdown.getItems().addAll(eventsItem, updatesItem, remindersItem);

        Button adminBtn = new Button("Admin");

        Button[] buttons = {homeBtn, aboutBtn, contactsBtn, announcementsBtn, adminBtn};

        for (Button b : buttons) {
            b.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        }

        // Base style for the dropdown
        dropdown.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        menuBar.getChildren().addAll(homeBtn, aboutBtn, contactsBtn, announcementsBtn, dropdown, spacer, adminBtn);

        // --- UNDERLINE ANIMATION ---
        underline = new Rectangle(60, 3, Color.web(ACCENT));
        StackPane underlinePane = new StackPane(underline);
        underlinePane.setAlignment(Pos.BOTTOM_LEFT);

        VBox navWrapper = new VBox(menuBar, underlinePane);
        navWrapper.setStyle("-fx-background-color: " + PRIMARY + ";");

        root.setTop(navWrapper);

        // --- DEFAULT PAGE ---
        root.setCenter(new HomePanel());

        // --- BUTTON ACTIONS ---
        homeBtn.setOnAction(e -> {
            root.setCenter(new HomePanel());
            moveUnderline(homeBtn);
        });

        aboutBtn.setOnAction(e -> {
            root.setCenter(new AboutPanel());
            moveUnderline(aboutBtn);
        });

        contactsBtn.setOnAction(e -> {
            root.setCenter(ContactsPanel.create());
            moveUnderline(contactsBtn);
        });
        
        announcementsBtn.setOnAction(e -> {
            root.setCenter(new AnnouncementsPanel());
            moveUnderline(announcementsBtn);
        });

        eventsItem.setOnAction(e -> root.setCenter(new EventsPanel()));
        updatesItem.setOnAction(e -> root.setCenter(new UpdatesPanel(getHostServices())));
        remindersItem.setOnAction(e -> root.setCenter(RemindersPanel.create()));

        adminBtn.setOnAction(e -> {
            openAdmin();
            moveUnderline(adminBtn);
        });

        Scene scene = new Scene(root, 1100, 650);
        primaryStage.setTitle("Agapay System");
        primaryStage.setScene(scene);
        primaryStage.show();

        // --- POST-SHOW FIXES ---
        Platform.runLater(() -> {
            // Fix 1: Initialize underline position
            moveUnderline(homeBtn);
            
            // Fix 2: Force "More" text color to white by looking up internal label
            if (dropdown.lookup(".label") != null) {
                dropdown.lookup(".label").setStyle("-fx-text-fill: white;");
            }
        });
    }

    private void moveUnderline(Button btn) {
        double targetX = btn.getLayoutX() + btn.getWidth() / 2 - underline.getWidth() / 2;
        TranslateTransition tt = new TranslateTransition(Duration.millis(250), underline);
        tt.setToX(targetX);
        tt.play();
    }

    private void openAdmin() {
        if (AdminSession.isLoggedIn()) {
            showDashboard(true); 
        } else {
            AdminLogin login = new AdminLogin();
            login.setLoginListener(() -> {
                showDashboard(login.isRememberMeChecked());
            });
        }
    }

    private void showDashboard(boolean rememberMePreference) {
        AdminSession.setLoggedIn(true, rememberMePreference);
        
        root.setCenter(new AdminDashboard(
                () -> {
                    AdminSession.setLoggedIn(false, false);
                    root.setCenter(new HomePanel());
                    moveUnderline(homeBtn);
                },
                () -> root.setCenter(new HomePanel()) 
        ));
    }
}   