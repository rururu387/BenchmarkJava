package com.goose.service;

import jakarta.inject.Singleton;
import oshi.SystemInfo;
import oshi.hardware.Sensors;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Singleton
public class TemperatureService {
    Map<LocalDateTime, List<Double>> temperatureMap;
    Sensors sensors;

    ScheduledThreadPoolExecutor executorService;

    public TemperatureService() {
        this.temperatureMap = new ConcurrentHashMap<>();

        SystemInfo systemInfo = new SystemInfo();
        sensors = systemInfo.getHardware().getSensors();
        executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(this::storeCpuTemperature, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void storeCpuTemperature() {
        temperatureMap.compute(LocalDateTime.now().withNano(0), (key, val) -> {
            if (val == null) {
                return new ArrayList<>() {{ add(sensors.getCpuTemperature()); }};
            }
            val.add(sensors.getCpuTemperature());
            return val;
        });
    }

    public Map<LocalDateTime, Double> getTemperatureMap() {
        return temperatureMap.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().withNano(0),
                entry -> entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(-1)));
    }
}
