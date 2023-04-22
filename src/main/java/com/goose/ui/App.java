package com.goose.ui;

import com.goose.conf.StartupScene;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class App extends AnnotationLiteral<StartupScene> {

    @Inject
    FXMLLoader fxmlLoader;

    public void start(@Observes Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            String fxmlDocPath = "src/main/resources/startupWindow.fxml";
            FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

            AnchorPane root = loader.load(fxmlStream);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}