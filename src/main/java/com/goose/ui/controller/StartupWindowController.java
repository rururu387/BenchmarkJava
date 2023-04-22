package com.goose.ui.controller;

import com.goose.ui.service.StartupWindowService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;

@Singleton
public class StartupWindowController {

    private static StartupWindowService service;

    @Inject
    public void setStartupWindowController(StartupWindowService service) {
        StartupWindowController.service = service;
    }

    void onStart(@Observes StartupEvent ev) {
    }

    @FXML
    private TextField benchmarkDurationInput;

    @FXML
    private Button confirmButton;

    @FXML
    private Text errorMessageField;

    @FXML
    private TextField processorLoadInput;

    @FXML
    private AnchorPane pane;

    @FXML
    void confirmReleased(MouseEvent event) {
        var timeOperationsMap = service.getBenchmarkResults(benchmarkDurationInput.getText(), processorLoadInput.getText(), errorMessageField);
        var timeTemperatureMap = service.getTemperatureMap();

        service.plotHeatEfficiencyMap(((Node)(event.getSource())).getScene().getWindow(), timeOperationsMap, timeTemperatureMap);
    }
}
