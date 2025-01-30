package com.github.avrokotlin.benchmark.lists

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.benchmark.internal.ListWrapperDatasClass
import kotlinx.benchmark.*
import java.util.concurrent.TimeUnit


@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@Measurement(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
internal abstract class SerializationListsBenchmark {
    lateinit var lists: ListWrapperDatasClass
    val schema = Avro.default.schema(ListWrapperDatasClass.serializer())

    @Setup
    fun initTestData() {
        setup()
        lists = ListWrapperDatasClass.create(10, 100000)
        prepareBinaryData()
    }

    abstract fun setup()

    abstract fun prepareBinaryData()
}