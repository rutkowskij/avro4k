package com.github.avrokotlin.benchmark.simple

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.benchmark.internal.SimpleDatasClass
import kotlinx.benchmark.Benchmark
import kotlinx.serialization.ExperimentalSerializationApi

internal class Avro4kV1SimpleBenchmark : SerializationSimpleBenchmark() {
    lateinit var data: ByteArray
    var writeMode = false

    override fun setup() {
    }

    override fun prepareBinaryData() {
        data = Avro.default.encodeToByteArray(SimpleDatasClass.serializer(), clients)
    }

    @Benchmark
    fun read() {
        if (writeMode) writeMode = false
        Avro.default.decodeFromByteArray(SimpleDatasClass.serializer(), data)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Benchmark
    fun write() {
        if (!writeMode) writeMode = true
        Avro.default.encodeToByteArray(SimpleDatasClass.serializer(), clients)
    }
}