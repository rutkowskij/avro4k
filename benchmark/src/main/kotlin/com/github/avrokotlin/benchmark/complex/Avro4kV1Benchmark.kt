package com.github.avrokotlin.benchmark.complex

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.benchmark.internal.Clients
import kotlinx.benchmark.Benchmark
import kotlinx.serialization.ExperimentalSerializationApi

internal class Avro4kV1Benchmark : SerializationBenchmark() {
    lateinit var data: ByteArray
    var writeMode = false

    override fun setup() {
    }

    override fun prepareBinaryData() {
        data = Avro.default.encodeToByteArray(Clients.serializer(), clients)
    }

    @Benchmark
    fun read() {
        if (writeMode) writeMode = false
        Avro.default.decodeFromByteArray(Clients.serializer(), data)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Benchmark
    fun write() {
        if (!writeMode) writeMode = true
        Avro.default.encodeToByteArray(Clients.serializer(), clients)
    }
}