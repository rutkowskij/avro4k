package com.sksamuel.avro4k.encoder

import kotlinx.serialization.CompositeEncoder
import kotlinx.serialization.ElementValueEncoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerializationException
import kotlinx.serialization.modules.SerialModule
import org.apache.avro.Schema

class MapEncoder(schema: Schema,
                 override val context: SerialModule,
                 private val desc: SerialDescriptor,
                 private val callback: (Map<String, *>) -> Unit) : ElementValueEncoder(),
   CompositeEncoder,
   StructureEncoder {

   private val map = mutableMapOf<String, Any>()
   private var key: String? = null
   private val valueSchema = schema.valueType

   override fun encodeString(value: String) {
      val k = key
      if (k == null) key = value else {
         map[k] = StringToAvroValue.toValue(valueSchema, value)
         key = null
      }
   }

   override fun encodeValue(value: Any) {
      val k = key
      if (k == null) throw SerializationException("Expected key but received value $value") else {
         map[k] = value
         key = null
      }
   }

   override fun endStructure(desc: SerialDescriptor) {
      callback(map.toMap())
   }

   override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeEncoder {
      return super<StructureEncoder>.beginStructure(desc, *typeParams)
   }

   override fun addValue(value: Any) {
      encodeValue(value)
   }

   override fun fieldSchema(): Schema = valueSchema
}
