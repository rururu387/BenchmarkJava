package com.goose.jni;

public class JNIBenchmarkRunner {
    public static native long[] runBenchmarkGetFPUProcessedOperationsCount(long benchmarkDurationMilliseconds, long cycleMilliseconds, float workloadRatio);
}
