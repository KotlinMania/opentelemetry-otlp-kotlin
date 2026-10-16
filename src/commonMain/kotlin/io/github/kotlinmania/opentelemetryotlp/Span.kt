// port-lint: source span.rs
package io.github.kotlinmania.opentelemetryotlp

import io.github.kotlinmania.opentelemetryotlp.exporter.Compression
import io.github.kotlinmania.opentelemetryotlp.exporter.ExportConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.http.HttpConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.http.OtlpHttpClient
import io.github.kotlinmania.opentelemetryotlp.exporter.tonic.TonicConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.tonic.TonicTracesClient
import kotlin.jvm.JvmName

/** Target to which the exporter is going to send spans, defaults to https://localhost:4317/v1/traces. */
public const val OTEL_EXPORTER_OTLP_TRACES_ENDPOINT: String = "OTEL_EXPORTER_OTLP_TRACES_ENDPOINT"

/** Max waiting time for the backend to process each spans batch, defaults to 10s. */
public const val OTEL_EXPORTER_OTLP_TRACES_TIMEOUT: String = "OTEL_EXPORTER_OTLP_TRACES_TIMEOUT"

/** Compression algorithm to use, defaults to none. */
public const val OTEL_EXPORTER_OTLP_TRACES_COMPRESSION: String = "OTEL_EXPORTER_OTLP_TRACES_COMPRESSION"

/** Key-value pairs to be used as headers associated with gRPC or HTTP requests for sending spans. */
public const val OTEL_EXPORTER_OTLP_TRACES_HEADERS: String = "OTEL_EXPORTER_OTLP_TRACES_HEADERS"

/** OTLP span exporter builder. */
public class SpanExporterBuilder<C>(
    public val client: C,
) {
    public fun withTonic(): SpanExporterBuilder<TonicExporterBuilderSet> =
        SpanExporterBuilder(TonicExporterBuilderSet())

    public fun withHttp(): SpanExporterBuilder<HttpExporterBuilderSet> =
        SpanExporterBuilder(HttpExporterBuilderSet())

    public companion object {
        /** Create a new [SpanExporterBuilder] with default settings. */
        public fun new(): SpanExporterBuilder<NoExporterBuilderSet> =
            SpanExporterBuilder(NoExporterBuilderSet())
    }
}

/** Build the [SpanExporter] with the gRPC Tonic transport. */
@JvmName("buildTonic")
public fun SpanExporterBuilder<TonicExporterBuilderSet>.build(): Result<SpanExporter> =
    client.builder.buildSpanExporter()

/** Build the [SpanExporter] with the HTTP transport. */
@JvmName("buildHttp")
public fun SpanExporterBuilder<HttpExporterBuilderSet>.build(): Result<SpanExporter> =
    client.builder.buildSpanExporter()

/** Return export config for Tonic builder. */
@JvmName("exportConfigTonic")
public fun SpanExporterBuilder<TonicExporterBuilderSet>.exportConfig(): ExportConfig =
    client.builder.exportConfig

/** Return export config for HTTP builder. */
@JvmName("exportConfigHttp")
public fun SpanExporterBuilder<HttpExporterBuilderSet>.exportConfig(): ExportConfig =
    client.builder.exportConfig

/** Return Tonic config. */
public fun SpanExporterBuilder<TonicExporterBuilderSet>.tonicConfig(): TonicConfig =
    client.builder.tonicConfig

/** Return HTTP client config. */
public fun SpanExporterBuilder<HttpExporterBuilderSet>.httpClientConfig(): HttpConfig =
    client.builder.httpConfig

@JvmName("withTonicEndpoint")
public fun SpanExporterBuilder<TonicExporterBuilderSet>.withEndpoint(endpoint: String): SpanExporterBuilder<TonicExporterBuilderSet> {
    client.builder.withEndpoint(endpoint)
    return this
}

@JvmName("withHttpEndpoint")
public fun SpanExporterBuilder<HttpExporterBuilderSet>.withEndpoint(endpoint: String): SpanExporterBuilder<HttpExporterBuilderSet> {
    client.builder.withEndpoint(endpoint)
    return this
}

public fun SpanExporterBuilder<TonicExporterBuilderSet>.withMetadata(metadata: Map<String, String>): SpanExporterBuilder<TonicExporterBuilderSet> {
    client.builder.withMetadata(metadata)
    return this
}

@JvmName("withTonicCompression")
public fun SpanExporterBuilder<TonicExporterBuilderSet>.withCompression(compression: Compression): SpanExporterBuilder<TonicExporterBuilderSet> {
    client.builder.withCompression(compression)
    return this
}

@JvmName("withHttpCompression")
public fun SpanExporterBuilder<HttpExporterBuilderSet>.withCompression(compression: Compression): SpanExporterBuilder<HttpExporterBuilderSet> {
    client.builder.withCompression(compression)
    return this
}

public fun SpanExporterBuilder<HttpExporterBuilderSet>.withHeaders(headers: Map<String, String>): SpanExporterBuilder<HttpExporterBuilderSet> {
    client.builder.withHeaders(headers)
    return this
}

/** Supported transport client variants. */
public sealed class SupportedTransportClient {
    public class Tonic(
        public val client: TonicTracesClient,
    ) : SupportedTransportClient()

    public class Http(
        public val client: OtlpHttpClient,
    ) : SupportedTransportClient()
}

/** OTLP exporter that sends tracing data. */
public class SpanExporter internal constructor(
    public val client: SupportedTransportClient,
) {
    public suspend fun export(batch: List<Any>): Result<Unit> =
        when (client) {
            is SupportedTransportClient.Tonic -> client.client.export(batch)
            is SupportedTransportClient.Http -> client.client.exportSpans(batch)
        }

    public fun setResource(resource: Any) {
        when (client) {
            is SupportedTransportClient.Tonic -> client.client.setResource(resource)
            is SupportedTransportClient.Http -> client.client.setResource(resource)
        }
    }

    public fun shutdown(): Result<Unit> =
        when (client) {
            is SupportedTransportClient.Tonic -> client.client.shutdown()
            is SupportedTransportClient.Http -> client.client.shutdown()
        }

    public companion object {
        /** Obtain a builder to configure a [SpanExporter]. */
        public fun builder(): SpanExporterBuilder<NoExporterBuilderSet> =
            SpanExporterBuilder.new()

        public fun fromHttp(client: OtlpHttpClient): SpanExporter =
            SpanExporter(SupportedTransportClient.Http(client))

        public fun fromTonic(client: TonicTracesClient): SpanExporter =
            SpanExporter(SupportedTransportClient.Tonic(client))
    }
}
