package com.github.avrokotlin.benchmark.simple

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.decodeFromGenericData
import com.github.avrokotlin.avro4k.encodeToByteArray
import com.github.avrokotlin.avro4k.encodeToGenericData
import com.github.avrokotlin.benchmark.internal.SimpleDatasClass
import kotlinx.benchmark.Benchmark
import kotlinx.serialization.ExperimentalSerializationApi
import org.apache.avro.Conversions
import org.apache.avro.generic.GenericData
import org.apache.avro.io.DatumReader
import org.apache.avro.io.DatumWriter
import org.apache.avro.io.DecoderFactory
import org.apache.avro.io.Encoder
import org.apache.avro.io.EncoderFactory
import java.io.ByteArrayInputStream
import java.io.OutputStream

internal class Avro4kGenericWithApacheAvroSimpleBenchmark : SerializationSimpleBenchmark() {
    lateinit var writer: DatumWriter<Any?>
    lateinit var encoder: Encoder
    lateinit var reader: DatumReader<Any?>

    lateinit var data: ByteArray
    var writeMode = false

    override fun setup() {
        GenericData.get().addLogicalTypeConversion(Conversions.DecimalConversion())
//        GenericData.get().addLogicalTypeConversion(TimeConversions.DateConversion())
//        GenericData.get().addLogicalTypeConversion(TimeConversions.TimestampMillisConversion())

        writer = GenericData.get().createDatumWriter(schema) as DatumWriter<Any?>
        encoder = EncoderFactory.get().directBinaryEncoder(OutputStream.nullOutputStream(), null)

        reader = GenericData.get().createDatumReader(schema) as DatumReader<Any?>
    }

    override fun prepareBinaryData() {
        data = Avro.encodeToByteArray(schema, clients)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Benchmark
    fun read() {
        if (writeMode) writeMode = false
        val decoder = DecoderFactory.get().directBinaryDecoder(ByteArrayInputStream(data), null)
        val genericData = reader.read(null, decoder)
        Avro.decodeFromGenericData<SimpleDatasClass>(schema, genericData)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Benchmark
    fun write() {
        if (!writeMode) writeMode = true
        val genericData = Avro.encodeToGenericData(schema, clients)
        writer.write(genericData, encoder)
    }
}