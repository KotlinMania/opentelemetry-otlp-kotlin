package io.github.kotlinmania.opentelemetryotlp.exporter

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
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/** The compression algorithm to use when sending data. */
@OptIn(ExperimentalObjCRefinement::class)
public enum class Compression : Serialize {
    /** Compresses data using gzip. */
    Gzip,

    /** Compresses data using zstd. */
    Zstd,
    ;

    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
        when (this) {
            Gzip -> serializer.serializeUnitVariant("Compression", 0u, "Gzip")
            Zstd -> serializer.serializeUnitVariant("Compression", 1u, "Zstd")
        }

    override fun toString(): String =
        when (this) {
            Gzip -> "gzip"
            Zstd -> "zstd"
        }

    public companion object : Deserialize<Compression> {
        private val VARIANTS: List<String> = listOf("Gzip", "Zstd")

        /**
         * Parse a [Compression] from its lowercase string form. Returns an
         * [ExporterBuildError.UnsupportedCompressionAlgorithm] for inputs that
         * are not recognized.
         */
        @HiddenFromObjC
        public fun fromString(s: String): Result<Compression> =
            when (s) {
                "gzip" -> Result.success(Gzip)
                "zstd" -> Result.success(Zstd)
                else -> Result.failure(ExporterBuildError.UnsupportedCompressionAlgorithm(s))
            }

        override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<Compression> {
            val fieldVisitor =
                object : Visitor<Compression>, DeserializeSeed<Compression> {
                    override fun expecting(): String = "variant identifier"

                    override fun visitU64(v: ULong): SerdeResult<Compression> =
                        when (v) {
                            0uL -> SerdeResult.success(Gzip)
                            1uL -> SerdeResult.success(Zstd)
                            else ->
                                SerdeResult.failure(
                                    SerdeError.invalidValue(
                                        Unexpected.Unsigned(v),
                                        object : Expected {
                                            override fun expecting(): String = "variant index 0 <= i < 2"
                                        },
                                    ),
                                )
                        }

                    override fun visitStr(v: String): SerdeResult<Compression> =
                        when (v) {
                            "Gzip" -> SerdeResult.success(Gzip)
                            "Zstd" -> SerdeResult.success(Zstd)
                            else -> SerdeResult.failure(SerdeError.unknownVariant(v, VARIANTS))
                        }

                    override fun visitBytes(v: ByteArray): SerdeResult<Compression> =
                        when (val decoded = serdeCatching { v.decodeToString(throwOnInvalidSequence = true) }) {
                            is SerdeResult.Success -> visitStr(decoded.value)
                            is SerdeResult.Failure ->
                                SerdeResult.failure(
                                    SerdeError.invalidValue(Unexpected.Bytes(v), this),
                                )
                        }

                    override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<Compression> =
                        deserializer.deserializeIdentifier(this)
                }

            val enumVisitor =
                object : Visitor<Compression> {
                    override fun expecting(): String = "enum Compression"

                    override fun <A> visitEnum(access: A): SerdeResult<Compression> where A : EnumAccess =
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

            return deserializer.deserializeEnum("Compression", VARIANTS, enumVisitor)
        }
    }
}
