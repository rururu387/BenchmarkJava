package com.goose.ui.service;

import jakarta.inject.Singleton;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class TimeTemperatureChartWindowService {

    public void drawTimeTemperatureChart(Stage stage, Map<Double, Double> timeTemperatureMap,
                                         double startBenchmarkTime, double endBenchmarkTime) {
        stage.setTitle("Time - temperature chart");

        final NumberAxis xAxis = new NumberAxis(-endBenchmarkTime * 0.005, (endBenchmarkTime - startBenchmarkTime) * 1.005, 1);
        xAxis.setLabel("Time, seconds");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("CPU temperature, degrees celsius");

        final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        timeTemperatureMap.forEach((key, val) -> series.getData().add(new XYChart.Data<>(key, val)));
        chart.getData().add(series);
        Scene scene = new Scene(chart, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }
}
