
module Agapay {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop; 
    requires java.sql;
    requires java.prefs;

    // This line is vital for JavaFX to launch your Main class
    opens agapay to javafx.graphics, javafx.fxml;
    exports agapay;
}
