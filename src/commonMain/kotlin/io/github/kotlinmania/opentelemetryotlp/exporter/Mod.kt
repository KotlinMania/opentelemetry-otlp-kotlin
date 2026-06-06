@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)
// port-lint: source src/exporter/mod.rs
package io.github.kotlinmania.opentelemetryotlp.exporter

import io.github.kotlinmania.opentelemetryotlp.Protocol
import kotlin.native.HiddenFromObjC
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * OTLP exporter builder and configurations.
 *
 * OTLP supports sending data via different protocols and formats.
 */

internal const val OTEL_EXPORTER_OTLP_PROTOCOL_HTTP_PROTOBUF: String = "http/protobuf"
internal const val OTEL_EXPORTER_OTLP_PROTOCOL_GRPC: String = "grpc"
internal const val OTEL_EXPORTER_OTLP_PROTOCOL_HTTP_JSON: String = "http/json"

// Endpoints per protocol https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/protocol/exporter.md
internal const val OTEL_EXPORTER_OTLP_GRPC_ENDPOINT_DEFAULT: String = "http://localhost:4317"
internal const val OTEL_EXPORTER_OTLP_HTTP_ENDPOINT_DEFAULT: String = "http://localhost:4318"

/**
 * Target to which the exporter is going to send signals, defaults to https://localhost:4317.
 * Learn about the relationship between this constant and metrics/spans/logs at
 * <https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/protocol/exporter.md#endpoint-urls-for-otlphttp>
 */
public const val OTEL_EXPORTER_OTLP_ENDPOINT: String = "OTEL_EXPORTER_OTLP_ENDPOINT"

/** Default target to which the exporter is going to send signals. */
public const val OTEL_EXPORTER_OTLP_ENDPOINT_DEFAULT: String = OTEL_EXPORTER_OTLP_HTTP_ENDPOINT_DEFAULT

/**
 * Key-value pairs to be used as headers associated with gRPC or HTTP requests
 * Example: `k1=v1,k2=v2`
 * Note: as of now, this is only supported for HTTP requests.
 */
public const val OTEL_EXPORTER_OTLP_HEADERS: String = "OTEL_EXPORTER_OTLP_HEADERS"

/** Protocol the exporter will use. Either `http/protobuf` or `grpc`. */
public const val OTEL_EXPORTER_OTLP_PROTOCOL: String = "OTEL_EXPORTER_OTLP_PROTOCOL"

/** Compression algorithm to use, defaults to none. */
public const val OTEL_EXPORTER_OTLP_COMPRESSION: String = "OTEL_EXPORTER_OTLP_COMPRESSION"

/**
 * Default protocol, using http-proto.
 *
 * Cargo-feature gating from upstream collapses to a single Kotlin default. The
 * upstream picks http-json when the http-json feature is on, http-protobuf when
 * http-proto is on (and http-json is off), grpc when only grpc-tonic is on,
 * and the empty string when no transport features are on. The Kotlin port
 * compiles every transport unconditionally, so the http-proto default applies.
 */
public const val OTEL_EXPORTER_OTLP_PROTOCOL_DEFAULT: String = OTEL_EXPORTER_OTLP_PROTOCOL_HTTP_PROTOBUF

/** Max waiting time for the backend to process each signal batch, defaults to 10 seconds. */
public const val OTEL_EXPORTER_OTLP_TIMEOUT: String = "OTEL_EXPORTER_OTLP_TIMEOUT"

/** Default max waiting time for the backend to process each signal batch. */
public val OTEL_EXPORTER_OTLP_TIMEOUT_DEFAULT: Duration = 10000.milliseconds

/** Configuration for the OTLP exporter. */
public class ExportConfig(
    /**
     * The address of the OTLP collector.
     * Default address will be used based on the protocol.
     *
     * Note: Programmatically setting this will override any value set via the environment variable.
     */
    public var endpoint: String? = null,

    /** The protocol to use when communicating with the collector. */
    public var protocol: Protocol = defaultProtocol(),

    /**
     * The timeout to the collector.
     * The default value is 10 seconds.
     *
     * Note: Programmatically setting this will override any value set via the environment variable.
     */
    public var timeout: Duration? = null,
) {
    override fun toString(): String =
        "ExportConfig(endpoint=$endpoint, protocol=$protocol, timeout=$timeout)"
}

/**
 * Errors that can occur while building an exporter.
 *
 * Marked non-exhaustive in upstream to allow for future expansion without
 * breaking changes. This could be refined after polishing and finalizing the
 * errors.
 */
@HiddenFromObjC
public sealed class ExporterBuildError(message: String) : Exception(message) {
    /** Spawning a new thread failed. */
    public object ThreadSpawnFailed :
        ExporterBuildError("Spawning a new thread failed. Unable to create Reqwest-Blocking client.")

    /** Feature required to use the specified compression algorithm. */
    public class FeatureRequiredForCompressionAlgorithm(
        public val feature: String,
        public val compression: Compression,
    ) : ExporterBuildError("feature '$feature' is required to use the compression algorithm '$compression'")

    /** No Http client specified. */
    public object NoHttpClient : ExporterBuildError("no http client specified")

    /** Unsupported compression algorithm. */
    public class UnsupportedCompressionAlgorithm(public val algorithm: String) :
        ExporterBuildError("unsupported compression algorithm '$algorithm'")

    /** Invalid URI. */
    public class InvalidUri(public val uri: String, public val reason: String) :
        ExporterBuildError("invalid URI $uri. Reason $reason")

    /**
     * Failed due to an internal error.
     * The error message is intended for logging purposes only and should not
     * be used to make programmatic decisions. It is implementation-specific
     * and subject to change without notice. Consumers of this error should not
     * rely on its content beyond logging.
     */
    public class InternalFailure(public val reason: String) : ExporterBuildError("Reason: $reason")
}

/** The compression algorithm to use when sending data. */
public enum class Compression {
    /** Compresses data using gzip. */
    Gzip,

    /** Compresses data using zstd. */
    Zstd,
    ;

    override fun toString(): String = when (this) {
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
        public fun fromString(s: String): Result<Compression> = when (s) {
            "gzip" -> Result.success(Gzip)
            "zstd" -> Result.success(Zstd)
            else -> Result.failure(ExporterBuildError.UnsupportedCompressionAlgorithm(s))
        }
    }
}

/** Default protocol based on enabled features. */
internal fun defaultProtocol(): Protocol = when (OTEL_EXPORTER_OTLP_PROTOCOL_DEFAULT) {
    OTEL_EXPORTER_OTLP_PROTOCOL_HTTP_PROTOBUF -> Protocol.HttpBinary
    OTEL_EXPORTER_OTLP_PROTOCOL_GRPC -> Protocol.Grpc
    OTEL_EXPORTER_OTLP_PROTOCOL_HTTP_JSON -> Protocol.HttpJson
    else -> Protocol.HttpBinary
}

/** Crate version, exposed by upstream through the CARGO_PKG_VERSION compile-time macro. */
private const val OTEL_OTLP_EXPORTER_CRATE_VERSION: String = "0.31.0"

/** Default user-agent headers. */
internal fun defaultHeaders(): MutableMap<String, String> {
    val headers = mutableMapOf<String, String>()
    headers["User-Agent"] = "OTel-OTLP-Exporter-Rust/$OTEL_OTLP_EXPORTER_CRATE_VERSION"
    return headers
}

/** Provide access to the [ExportConfig] field within the exporter builders. */
public interface HasExportConfig {
    /** Return a mutable reference to the [ExportConfig] within the exporter builders. */
    public var exportConfig: ExportConfig
}

/**
 * Expose methods to override [ExportConfig].
 *
 * This trait will be implemented for every struct that implemented [HasExportConfig] trait.
 *
 * Example:
 * ```
 * val exporterBuilder = SpanExporter.builder()
 *     .withTonic()
 *     .withEndpoint("http://localhost:7201")
 * ```
 */
public interface WithExportConfig<T : WithExportConfig<T>> {
    /**
     * Set the address of the OTLP collector. If not set or set to empty string, the default address is used.
     *
     * Note: Programmatically setting this will override any value set via the environment variable.
     */
    public fun withEndpoint(endpoint: String): T

    /**
     * Set the protocol to use when communicating with the collector.
     *
     * Note that protocols that are not supported by exporters will be ignored. The exporter
     * will use default protocol in this case.
     *
     * All exporters in this crate only support one protocol, thus choosing the protocol is a no-op at the moment.
     */
    public fun withProtocol(protocol: Protocol): T

    /**
     * Set the timeout to the collector.
     *
     * Note: Programmatically setting this will override any value set via the environment variable.
     */
    public fun withTimeout(timeout: Duration): T

    /**
     * Set export config. This will override all previous configurations.
     *
     * Note: Programmatically setting this will override any value set via environment variables.
     */
    public fun withExportConfig(exportConfig: ExportConfig): T
}

/**
 * Blanket-implementation extensions providing [WithExportConfig] behavior for any
 * builder that exposes a mutable [HasExportConfig.exportConfig]. Concrete builder
 * classes declare their `WithExportConfig` conformance and delegate to these
 * helpers; the file is structured the same way as the upstream blanket impl
 * `impl<B: HasExportConfig> WithExportConfig for B`.
 */
public fun <T : HasExportConfig> T.applyEndpoint(endpoint: String): T {
    exportConfig.endpoint = endpoint
    return this
}

public fun <T : HasExportConfig> T.applyProtocol(protocol: Protocol): T {
    exportConfig.protocol = protocol
    return this
}

public fun <T : HasExportConfig> T.applyTimeout(timeout: Duration): T {
    exportConfig.timeout = timeout
    return this
}

public fun <T : HasExportConfig> T.applyExportConfig(exporterConfig: ExportConfig): T {
    exportConfig.endpoint = exporterConfig.endpoint
    exportConfig.protocol = exporterConfig.protocol
    exportConfig.timeout = exporterConfig.timeout
    return this
}

/**
 * URL-decode percent-escaped sequences in [value]. Returns null when [value]
 * contains an incomplete or non-hex percent escape, mirroring the upstream
 * `Option<String>` return type.
 */
internal fun urlDecode(value: String): String? {
    val result = StringBuilder(value.length)
    val charsToDecode = mutableListOf<Byte>()
    val all = value.iterator()

    while (true) {
        val ch: Char? = if (all.hasNext()) all.nextChar() else null

        if (ch == '%') {
            val hi = if (all.hasNext()) all.nextChar() else return null
            val lo = if (all.hasNext()) all.nextChar() else return null
            val byte = "$hi$lo".toIntOrNull(16) ?: return null
            charsToDecode.add(byte.toByte())
            continue
        }

        if (charsToDecode.isNotEmpty()) {
            val bytes = charsToDecode.toByteArray()
            result.append(bytes.decodeToString(throwOnInvalidSequence = true))
            charsToDecode.clear()
        }

        if (ch != null) {
            result.append(ch)
        } else {
            return result.toString()
        }
    }
}

/**
 * Parse one `key=value` segment of an OTLP headers env var. Returns null when
 * the segment has no `=`, an empty key, or an empty value, matching the
 * upstream filter behavior.
 */
internal fun parseHeaderKeyValueString(keyValueString: String): Pair<String, String>? {
    val eq = keyValueString.indexOf('=')
    if (eq < 0) return null
    val key = keyValueString.substring(0, eq).trim()
    val rawValue = keyValueString.substring(eq + 1).trim()
    val value = urlDecode(rawValue) ?: rawValue
    if (key.isEmpty() || value.isEmpty()) return null
    return key to value
}

/**
 * Parse the OTLP headers env var format, a comma-separated list of `key=value`
 * pairs. Trims surrounding whitespace from each segment and skips empty
 * segments, matching upstream `split_terminator`.
 */
internal fun parseHeaderString(value: String): Sequence<Pair<String, String>> =
    value.splitToSequence(',')
        .filter { it.isNotEmpty() }
        .map { it.trim() }
        .mapNotNull { parseHeaderKeyValueString(it) }
