package io.github.kotlinmania.opentelemetryotlp

import io.github.kotlinmania.serde.SerdeError
import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serde.serdeCatching
import io.github.kotlinmania.serdecore.de.Deserialize
import io.github.kotlinmania.serdecore.de.DeserializeSeed
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.EnumAccess
import io.github.kotlinmania.serdecore.de.Expected
import io.github.kotlinmania.serdecore.de.Unexpected
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.Serializer

/**
 * The communication protocol to use when exporting data.
 */
public enum class Protocol : Serialize {
    /** GRPC protocol */
    Grpc,

    /** HTTP protocol with binary protobuf */
    HttpBinary,

    /** HTTP protocol with JSON payload */
    HttpJson,
    ;

    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
        when (this) {
            Grpc -> serializer.serializeUnitVariant("Protocol", 0u, "Grpc")
            HttpBinary -> serializer.serializeUnitVariant("Protocol", 1u, "HttpBinary")
            HttpJson -> serializer.serializeUnitVariant("Protocol", 2u, "HttpJson")
        }

    public companion object : Deserialize<Protocol> {
        private val VARIANTS: List<String> = listOf("Grpc", "HttpBinary", "HttpJson")

        override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<Protocol> {
            val fieldVisitor =
                object : Visitor<Protocol>, DeserializeSeed<Protocol> {
                    override fun expecting(): String = "variant identifier"

                    override fun visitU64(v: ULong): SerdeResult<Protocol> =
                        when (v) {
                            0uL -> SerdeResult.success(Grpc)
                            1uL -> SerdeResult.success(HttpBinary)
                            2uL -> SerdeResult.success(HttpJson)
                            else ->
                                SerdeResult.failure(
                                    SerdeError.invalidValue(
                                        Unexpected.Unsigned(v),
                                        object : Expected {
                                            override fun expecting(): String = "variant index 0 <= i < 3"
                                        },
                                    ),
                                )
                        }

                    override fun visitStr(v: String): SerdeResult<Protocol> =
                        when (v) {
                            "Grpc" -> SerdeResult.success(Grpc)
                            "HttpBinary" -> SerdeResult.success(HttpBinary)
                            "HttpJson" -> SerdeResult.success(HttpJson)
                            else -> SerdeResult.failure(SerdeError.unknownVariant(v, VARIANTS))
                        }

                    override fun visitBytes(v: ByteArray): SerdeResult<Protocol> =
                        when (val decoded = serdeCatching { v.decodeToString(throwOnInvalidSequence = true) }) {
                            is SerdeResult.Success -> visitStr(decoded.value)
                            is SerdeResult.Failure ->
                                SerdeResult.failure(
                                    SerdeError.invalidValue(Unexpected.Bytes(v), this),
                                )
                        }

                    override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<Protocol> =
                        deserializer.deserializeIdentifier(this)
                }

            val enumVisitor =
                object : Visitor<Protocol> {
                    override fun expecting(): String = "enum Protocol"

                    override fun <A> visitEnum(access: A): SerdeResult<Protocol> where A : EnumAccess =
                        when (val res = access.variantSeed(fieldVisitor)) {
                            is SerdeResult.Failure -> SerdeResult.failure(res.error)
                            is SerdeResult.Success -> {
                                val (variant, variantAccess) = res.value
                                when (val unitRes = variantAccess.unitVariant()) {
                                    is SerdeResult.Failure -> SerdeResult.failure(unitRes.error)
                                    is SerdeResult.Success -> SerdeResult.success(variant)
                                }
                            }
                        }
                }

            return deserializer.deserializeEnum("Protocol", VARIANTS, enumVisitor)
        }
    }
}
