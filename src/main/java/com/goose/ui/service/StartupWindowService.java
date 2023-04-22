package com.goose.ui.service;

import com.goose.service.BenchmarkService;
import com.goose.service.TemperatureService;
import jakarta.inject.Singleton;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class StartupWindowService {

    BenchmarkService benchmarkService;
    TemperatureService temperatureService;

    TimeEfficiencyChartWindowService timeEfficiencyService;

    TimeTemperatureChartWindowService timeTemperatureService;

    TemperatureEfficiencyChartWindowService temperatureEfficiencyService;

    public StartupWindowService(BenchmarkService benchmarkService, TemperatureService temperatureService,
                                TimeEfficiencyChartWindowService timeEfficiencyService,
                                TimeTemperatureChartWindowService timeTemperatureService,
                                TemperatureEfficiencyChartWindowService temperatureEfficiencyService) {
        this.benchmarkService = benchmarkService;
        this.temperatureService = temperatureService;
        this.timeEfficiencyService = timeEfficiencyService;
        this.timeTemperatureService = timeTemperatureService;
        this.temperatureEfficiencyService = temperatureEfficiencyService;
    }

    public Map<LocalDateTime, Long> getBenchmarkResults(String benchmarkDurationStr, String workloadRatioStr, Text errorMessageField) {
        try {
            long benchmarkDuration = Long.parseLong(benchmarkDurationStr);
            float workloadRatio = Float.parseFloat(workloadRatioStr);

            return benchmarkService.runBenchmark(benchmarkDuration, 1000, workloadRatio);
        }
        catch (Exception e) {
            errorMessageField.setText("Некорректный ввод");
            return null;
        }
    }

    public Map<LocalDateTime, Double> getTemperatureMap() {
        return temperatureService.getTemperatureMap();
    }

    public void plotHeatEfficiencyMap(Window currentWindow, Map<LocalDateTime, Long> localTimeOperationsMap, Map<LocalDateTime, Double> localTimeTemperatureMap) {
        var startBenchmarkLocalTime = localTimeOperationsMap.keySet().stream().min(Comparator.naturalOrder()).get();
        var endBenchmarkLocalTime = localTimeOperationsMap.keySet().stream().max(Comparator.naturalOrder()).get();

        var startBenchmarkTime = (double) (ZonedDateTime.of(startBenchmarkLocalTime, ZoneId.systemDefault()).toInstant().toEpochMilli());
        var startBenchmarkTimeRounded = (double) (ZonedDateTime.of(startBenchmarkLocalTime.withNano(0), ZoneId.systemDefault()).toInstant().toEpochMilli());
        var endBenchmarkTime = (double) (ZonedDateTime.of(endBenchmarkLocalTime, ZoneId.systemDefault()).toInstant().toEpochMilli());

        var timeOperationsMap = localTimeOperationsMap.entrySet().stream().collect(Collectors.toMap(
                entry -> (ZonedDateTime.of(entry.getKey(), ZoneId.systemDefault()).toInstant().toEpochMilli() - startBenchmarkTime) / 1000,
                Map.Entry::getValue));

        var timeTemperatureMap = localTimeTemperatureMap.entrySet().stream().filter(entry ->
                !entry.getKey().isBefore(startBenchmarkLocalTime) && !entry.getKey().isAfter(endBenchmarkLocalTime))
                .collect(Collectors.toMap(entry ->
                                (ZonedDateTime.of(entry.getKey(), ZoneId.systemDefault()).toInstant().toEpochMilli() - startBenchmarkTimeRounded) / 1000,
                                                                                                Map.Entry::getValue));


        Stage stage = new Stage();
        timeEfficiencyService.drawTimeEfficiencyChart(stage, timeOperationsMap, 0, (endBenchmarkTime - startBenchmarkTime) / 1000);

        Stage stage2 = new Stage();
        timeTemperatureService.drawTimeTemperatureChart(stage2, timeTemperatureMap, 0, (endBenchmarkTime - startBenchmarkTime) / 1000);

        Stage stage3 = new Stage();
        temperatureEfficiencyService.drawTemperatureEfficiencyChart(stage3, timeTemperatureMap, timeOperationsMap);

        currentWindow.hide();
    }
}
