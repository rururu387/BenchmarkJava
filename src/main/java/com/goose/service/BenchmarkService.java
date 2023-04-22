package com.goose.service;

import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class BenchmarkService {
    static {
        String libraryPath = "/home/lavrentiy/Документы/Работа/МИЭТ/Benchmark/build-benchmarkCore-Desktop-Release/benchmarkCore.so";
        System.load(libraryPath);
    }
    private static int availableThreads;

    TemperatureService temperatureService;

    public BenchmarkService(TemperatureService temperatureService) {
        availableThreads = Runtime.getRuntime().availableProcessors();
        this.temperatureService = temperatureService;
    }

    public Map<LocalDateTime, Long> runBenchmark(long durationMilliseconds, long cycleMilliseconds, float workloadRatio) {
        var benchmarkResult = new HashMap<LocalDateTime, Long>();
        var benchmarkTasks = new ArrayList<BenchmarkTask>();
        for (int i = 0; i < availableThreads; i++) {
            var benchmarkTask = new BenchmarkTask(durationMilliseconds, cycleMilliseconds, workloadRatio);
            benchmarkTasks.add(benchmarkTask);
        }

        LocalDateTime startDateTime = LocalDateTime.now();

        // TODO - use real-time OS, kinda crunch
        for (var benchmarkTask : benchmarkTasks) {
            benchmarkTask.start();
        }

        for (var benchmarkTask : benchmarkTasks) {
            try {
                benchmarkTask.join();
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        for (int i = 0; i < availableThreads; i++) {
            var benchmarkResultArray = benchmarkTasks.get(i).getResultArray();
            for (int j = 0; j < benchmarkResultArray.length; j++) {
                int finalJ = j;
                benchmarkResult.compute(startDateTime.plusNanos(j * 1_000_000 * cycleMilliseconds),
                        (key, val) -> val == null ? benchmarkResultArray[finalJ] : val + benchmarkResultArray[finalJ]);
            }
        }

        return benchmarkResult.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() / cycleMilliseconds * 1000));
    }
}
