// port-lint: source exporter/mod.rs
package io.github.kotlinmania.opentelemetryotlp.exporter

import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/** The compression algorithm to use when sending data. */
@OptIn(ExperimentalObjCRefinement::class)
public enum class Compression {
    /** Compresses data using gzip. */
    Gzip,

    /** Compresses data using zstd. */
    Zstd,
    ;

    override fun toString(): String =
        when (this) {
            Gzip -> "gzip"
            Zstd -> "zstd"
        }

    public companion object {
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
    }
}
