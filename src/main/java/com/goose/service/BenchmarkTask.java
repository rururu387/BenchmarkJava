package com.goose.service;

import com.goose.jni.JNIBenchmarkRunner;

public class BenchmarkTask extends Thread {
    private volatile long[] resultArray;

    long durationMilliseconds;
    long cycleMilliseconds;
    float workloadPercentage;

    public BenchmarkTask(long durationMilliseconds, long cycleMilliseconds, float workloadPercentage) {
        this.durationMilliseconds = durationMilliseconds;
        this.cycleMilliseconds = cycleMilliseconds;
        this.workloadPercentage = workloadPercentage;
    }

    @Override
    public void run() {
        resultArray = JNIBenchmarkRunner.runBenchmarkGetFPUProcessedOperationsCount(durationMilliseconds, cycleMilliseconds, workloadPercentage);
    }

    public long[] getResultArray() {
        return resultArray;
    }
}
