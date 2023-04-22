package com.goose.ui.service;

import jakarta.inject.Singleton;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Map;

@Singleton
public class TimeEfficiencyChartWindowService {

    public void drawTimeEfficiencyChart(Stage stage, Map<Double, Long> timeOperationsMap, double startBenchmarkTime,
                                        double endBenchmarkTime) {
        stage.setTitle("Time - efficiency chart");
        final NumberAxis xAxis = new NumberAxis(-(endBenchmarkTime - startBenchmarkTime) * 0.005,
                (endBenchmarkTime - startBenchmarkTime) * 1.005, 1);
        xAxis.setLabel("Time, seconds");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount of FPU operations processed in last second");

        final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        timeOperationsMap.forEach((key, val) -> series.getData().add(new XYChart.Data<>((key - startBenchmarkTime), val)));
        chart.getData().add(series);
        Scene scene = new Scene(chart, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }
}
