package agapay;

import javafx.beans.property.*;

public class Post {
    private IntegerProperty id;
    private StringProperty imagePath;
    private StringProperty caption;
    private StringProperty type;
    private StringProperty date;

    public Post(int id, String imagePath, String caption, String type, String date) {
        this.id = new SimpleIntegerProperty(id);
        this.imagePath = new SimpleStringProperty(imagePath);
        this.caption = new SimpleStringProperty(caption);
        this.type = new SimpleStringProperty(type);
        this.date = new SimpleStringProperty(date);
    }

    public int getId() { return id.get(); }
    public String getImagePath() { return imagePath.get(); }
    public String getCaption() { return caption.get(); }
    public String getType() { return type.get(); }
    public String getDate() { return date.get(); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty imagePathProperty() { return imagePath; }
    public StringProperty captionProperty() { return caption; }
    public StringProperty typeProperty() { return type; }
    public StringProperty dateProperty() { return date; }
}
