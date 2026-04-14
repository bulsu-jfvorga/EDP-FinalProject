package agapay;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CreatePostPanel extends ScrollPane {
    private final Runnable refreshCallback;

    public CreatePostPanel(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
        setFitToWidth(true);
        
        VBox mainContainer = new VBox(25);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setAlignment(Pos.TOP_CENTER);

        VBox formCard = new VBox(20);
        formCard.setMaxWidth(600);
        formCard.setPadding(new Insets(30));
        formCard.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("event", "announcement");
        typeBox.setPromptText("Select Category");

        TextArea captionArea = new TextArea();
        captionArea.setPromptText("Enter post caption here...");
        captionArea.setWrapText(true);

        ImageView imgView = new ImageView();
        imgView.setFitWidth(200);
        imgView.setPreserveRatio(true);
        
        final String[] selectedPath = {""};
        Button browseBtn = new Button("SELECT IMAGE");
        browseBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(null);
            if (file != null) {
                selectedPath[0] = file.getAbsolutePath();
                imgView.setImage(new Image(file.toURI().toString()));
            }
        });

        Button postBtn = new Button("PUBLISH POST");
        postBtn.setStyle("-fx-background-color: #800000; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30;");
        postBtn.setOnAction(e -> executePost(typeBox, captionArea, selectedPath, imgView));

        formCard.getChildren().addAll(new Label("Post Type:"), typeBox, new Label("Caption:"), captionArea, browseBtn, imgView, postBtn);
        mainContainer.getChildren().add(formCard);
        setContent(mainContainer);
    }

    private void executePost(ComboBox<String> typeBox, TextArea captionArea, String[] path, ImageView iv) {
        if (typeBox.getValue() == null || captionArea.getText().trim().isEmpty()) return;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO posts (image_path, caption, type) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, path[0]); 
            pst.setString(2, captionArea.getText()); 
            pst.setString(3, typeBox.getValue());
            
            if (pst.executeUpdate() > 0) {
                captionArea.clear(); 
                iv.setImage(null);
                path[0] = ""; 
                if(refreshCallback != null) refreshCallback.run();
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
