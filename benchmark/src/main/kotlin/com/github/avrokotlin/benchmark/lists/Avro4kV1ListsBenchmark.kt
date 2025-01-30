package com.github.avrokotlin.benchmark.lists

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.benchmark.internal.ListWrapperDatasClass
import kotlinx.benchmark.Benchmark
import kotlinx.serialization.ExperimentalSerializationApi

internal class Avro4kV1ListsBenchmark : SerializationListsBenchmark() {
    lateinit var data: ByteArray
    var writeMode = false

    override fun setup() {
    }

    override fun prepareBinaryData() {
        data = Avro.default.encodeToByteArray(ListWrapperDatasClass.serializer(), lists)
    }

    @Benchmark
    fun read() {
        if (writeMode) writeMode = false
        Avro.default.decodeFromByteArray(ListWrapperDatasClass.serializer(), data)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Benchmark
    fun write() {
        if (!writeMode) writeMode = true
        Avro.default.encodeToByteArray(ListWrapperDatasClass.serializer(), lists)
    }
}