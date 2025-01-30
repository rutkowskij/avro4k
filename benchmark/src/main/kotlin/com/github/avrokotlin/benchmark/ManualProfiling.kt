package com.github.avrokotlin.benchmark

import com.github.avrokotlin.benchmark.complex.Avro4kV1Benchmark
import com.github.avrokotlin.benchmark.lists.Avro4kV1ListsBenchmark

internal object ManualProfilingRead {
    @JvmStatic
    fun main(vararg args: String) {
        Avro4kV1Benchmark().apply {
            initTestData()
            for (i in 0 until 1_000_000) {
                if (i % 1_000 == 0) println("Iteration $i")
                read()
            }
        }
    }
}

internal object ManualProfilingListRead {
    @JvmStatic
    fun main(vararg args: String) {
        Avro4kV1ListsBenchmark().apply {
            initTestData()
            for (i in 0 until 1_000) {
                if (i % 100 == 0) println("Iteration $i")
                read()
            }
        }
    }
}