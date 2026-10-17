// port-lint: source span.rs
package io.github.kotlinmania.opentelemetryotlp

import io.github.kotlinmania.opentelemetryotlp.exporter.Compression
import io.github.kotlinmania.opentelemetryotlp.exporter.ExportConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.HasExportConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.http.HttpConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.http.OtlpHttpClient
import io.github.kotlinmania.opentelemetryotlp.exporter.http.exportTrace
import io.github.kotlinmania.opentelemetryotlp.exporter.http.setTraceResource
import io.github.kotlinmania.opentelemetryotlp.exporter.http.shutdownTrace
import io.github.kotlinmania.opentelemetryotlp.exporter.tonic.TonicConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.tonic.TonicTracesClient

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

private fun build(client: TonicExporterBuilderSet): Result<SpanExporter> =
    client.builder.buildSpanExporter()

private fun build(client: HttpExporterBuilderSet): Result<SpanExporter> =
    client.builder.buildSpanExporter()

/** Build the [SpanExporter] with the configured transport. */
public fun <C : HasExportConfig> SpanExporterBuilder<C>.build(): Result<SpanExporter> =
    when (val c = client) {
        is TonicExporterBuilderSet -> build(c)
        is HttpExporterBuilderSet -> build(c)
        else -> Result.failure(IllegalStateException("Unsupported exporter builder set"))
    }

private fun exportConfig(client: TonicExporterBuilderSet): ExportConfig =
    client.builder.exportConfig

private fun exportConfig(client: HttpExporterBuilderSet): ExportConfig =
    client.builder.exportConfig

/** Return export config for the configured builder. */
public fun <C : HasExportConfig> SpanExporterBuilder<C>.exportConfig(): ExportConfig =
    when (val c = client) {
        is TonicExporterBuilderSet -> exportConfig(c)
        is HttpExporterBuilderSet -> exportConfig(c)
        else -> client.exportConfig
    }

/** Return Tonic config. */
public fun SpanExporterBuilder<TonicExporterBuilderSet>.tonicConfig(): TonicConfig =
    client.builder.tonicConfig

/** Return HTTP client config. */
public fun SpanExporterBuilder<HttpExporterBuilderSet>.httpClientConfig(): HttpConfig =
    client.builder.httpConfig

public fun <C : HasExportConfig> SpanExporterBuilder<C>.withEndpoint(endpoint: String): SpanExporterBuilder<C> {
    client.exportConfig.endpoint = endpoint
    return this
}

public fun SpanExporterBuilder<TonicExporterBuilderSet>.withMetadata(metadata: Map<String, String>): SpanExporterBuilder<TonicExporterBuilderSet> {
    client.builder.withMetadata(metadata)
    return this
}

public fun <C : HasExportConfig> SpanExporterBuilder<C>.withCompression(compression: Compression): SpanExporterBuilder<C> {
    when (val c = client) {
        is TonicExporterBuilderSet -> c.builder.withCompression(compression)
        is HttpExporterBuilderSet -> c.builder.withCompression(compression)
    }
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
            is SupportedTransportClient.Http -> client.client.exportTrace(batch)
        }

    public fun setResource(resource: Any) {
        when (client) {
            is SupportedTransportClient.Tonic -> client.client.setResource(resource)
            is SupportedTransportClient.Http -> client.client.setTraceResource(resource)
        }
    }

    public fun shutdown(): Result<Unit> =
        when (client) {
            is SupportedTransportClient.Tonic -> client.client.shutdown()
            is SupportedTransportClient.Http -> client.client.shutdownTrace()
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
