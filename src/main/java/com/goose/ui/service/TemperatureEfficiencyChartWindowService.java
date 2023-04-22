package com.goose.ui.service;

import jakarta.inject.Singleton;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.Map;

@Singleton
public class TemperatureEfficiencyChartWindowService {
    public void drawTemperatureEfficiencyChart(Stage stage, Map<Double, Double> timeTemperatureMap,
                                               Map<Double, Long> timeOperationsMap) {
        stage.setTitle("Temperature - efficiency chart");

        var minTemperature = timeTemperatureMap.values().stream().min(Comparator.naturalOrder()).get();
        var maxTemperature = timeTemperatureMap.values().stream().max(Comparator.naturalOrder()).get();

        final NumberAxis xAxis = new NumberAxis(minTemperature - ((maxTemperature - minTemperature) * 0.005),
                maxTemperature + ((maxTemperature - minTemperature) * 0.005), 1);
        xAxis.setLabel("CPU temperature, degrees celsius");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount of FPU operations processed in last second");

        final ScatterChart<Number, Number> chart = new ScatterChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        timeTemperatureMap.forEach((key, val) -> series.getData().add(new XYChart.Data<>(val, timeOperationsMap.get(key))));
        chart.getData().add(series);
        Scene scene = new Scene(chart, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }
}
