// port-lint: source exporter/http/mod.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.http

import io.github.kotlinmania.opentelemetryotlp.exporter.Compression
import io.github.kotlinmania.opentelemetryotlp.exporter.ExporterBuildError
import io.github.kotlinmania.opentelemetryotlp.exporter.parseHeaderString

/**
 * Configuration of the http transport.
 */
public class HttpConfig(
    public var headers: Map<String, String>? = null,
    public var compression: Compression? = null,
)

public const val OTEL_EXPORTER_OTLP_HTTP_ENDPOINT_DEFAULT: String = "http://localhost:4318"

/** Provide access to the [HttpConfig] field within the exporter builders. */
public interface HasHttpConfig {
    public fun httpClientConfig(): HttpConfig
}

/** Expose methods to override [HttpConfig]. */
public interface WithHttpConfig<T : WithHttpConfig<T>> {
    public fun withHeaders(headers: Map<String, String>): T

    public fun withCompression(compression: Compression): T
}

public fun buildEndpointUri(endpoint: String, path: String): Result<String> {
    val cleanPath =
        if (endpoint.endsWith('/') && path.startsWith('/')) {
            path.substring(1)
        } else {
            path
        }
    val combined = "$endpoint$cleanPath"
    return if (combined.startsWith("http://") || combined.startsWith("https://")) {
        Result.success(combined)
    } else {
        Result.failure(ExporterBuildError.InvalidUri(combined, "invalid URI"))
    }
}

public fun resolveHttpEndpoint(
    signalEndpointVar: String,
    signalEndpointPath: String,
    providedEndpoint: String?,
): Result<String> {
    if (!providedEndpoint.isNullOrEmpty()) {
        return buildEndpointUri(providedEndpoint, "")
    }
    return buildEndpointUri(OTEL_EXPORTER_OTLP_HTTP_ENDPOINT_DEFAULT, signalEndpointPath)
}

public fun addHeaderFromString(input: String, headers: MutableMap<String, String>) {
    for ((key, value) in parseHeaderString(input)) {
        headers[key] = value
    }
}
