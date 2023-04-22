package com.goose;

import jakarta.enterprise.inject.spi.CDI;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class FxApplication extends Application {
    public FxApplication() {
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        CDI.current().getBeanManager().getEvent().fire(primaryStage);
    }
}
