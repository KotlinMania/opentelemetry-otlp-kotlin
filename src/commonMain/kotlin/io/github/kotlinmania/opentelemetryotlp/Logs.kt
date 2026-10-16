// port-lint: source logs.rs
package io.github.kotlinmania.opentelemetryotlp

import io.github.kotlinmania.opentelemetryotlp.exporter.ExportConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.http.HttpConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.http.OtlpHttpClient
import io.github.kotlinmania.opentelemetryotlp.exporter.tonic.TonicConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.tonic.TonicLogsClient
import kotlin.time.Duration

/** Compression algorithm to use, defaults to none. */
public const val OTEL_EXPORTER_OTLP_LOGS_COMPRESSION: String = "OTEL_EXPORTER_OTLP_LOGS_COMPRESSION"

/** Target to which the exporter is going to send logs. */
public const val OTEL_EXPORTER_OTLP_LOGS_ENDPOINT: String = "OTEL_EXPORTER_OTLP_LOGS_ENDPOINT"

/** Maximum time the OTLP exporter will wait for each batch logs export. */
public const val OTEL_EXPORTER_OTLP_LOGS_TIMEOUT: String = "OTEL_EXPORTER_OTLP_LOGS_TIMEOUT"

/** Key-value pairs to be used as headers associated with gRPC or HTTP requests for sending logs. */
public const val OTEL_EXPORTER_OTLP_LOGS_HEADERS: String = "OTEL_EXPORTER_OTLP_LOGS_HEADERS"

/** Builder for creating a new [LogExporter]. */
public class LogExporterBuilder<C>(
    public val client: C,
    public val endpoint: String? = null,
) {
    public fun withTonic(): LogExporterBuilder<TonicExporterBuilderSet> =
        LogExporterBuilder(TonicExporterBuilderSet(), endpoint)

    public fun withHttp(): LogExporterBuilder<HttpExporterBuilderSet> =
        LogExporterBuilder(HttpExporterBuilderSet(), endpoint)

    public companion object {
        /** Create a new [LogExporterBuilder] with default settings. */
        public fun new(): LogExporterBuilder<NoExporterBuilderSet> =
            LogExporterBuilder(NoExporterBuilderSet())
    }
}

/** Build the [LogExporter] with the gRPC Tonic transport. */
@kotlin.jvm.JvmName("buildTonic")
public fun LogExporterBuilder<TonicExporterBuilderSet>.build(): Result<LogExporter> =
    client.builder.buildLogExporter()

/** Build the [LogExporter] with the HTTP transport. */
@kotlin.jvm.JvmName("buildHttp")
public fun LogExporterBuilder<HttpExporterBuilderSet>.build(): Result<LogExporter> =
    client.builder.buildLogExporter()

/** Return export config for Tonic builder. */
@kotlin.jvm.JvmName("exportConfigTonic")
public fun LogExporterBuilder<TonicExporterBuilderSet>.exportConfig(): ExportConfig =
    client.builder.exportConfig

/** Return export config for HTTP builder. */
@kotlin.jvm.JvmName("exportConfigHttp")
public fun LogExporterBuilder<HttpExporterBuilderSet>.exportConfig(): ExportConfig =
    client.builder.exportConfig

/** Return Tonic config. */
public fun LogExporterBuilder<TonicExporterBuilderSet>.tonicConfig(): TonicConfig =
    client.builder.tonicConfig

/** Return HTTP client config. */
public fun LogExporterBuilder<HttpExporterBuilderSet>.httpClientConfig(): HttpConfig =
    client.builder.httpConfig

/** OTLP exporter that sends log data. */
public class LogExporter internal constructor(
    public val client: SupportedTransportClient,
) {
    public sealed class SupportedTransportClient {
        public class Tonic(
            public val client: TonicLogsClient,
        ) : SupportedTransportClient()

        public class Http(
            public val client: OtlpHttpClient,
        ) : SupportedTransportClient()
    }

    public suspend fun export(batch: List<Any>): Result<Unit> =
        when (client) {
            is SupportedTransportClient.Tonic -> client.client.export(batch)
            is SupportedTransportClient.Http -> client.client.exportLogs(batch)
        }

    public fun setResource(resource: Any) {
        when (client) {
            is SupportedTransportClient.Tonic -> client.client.setResource(resource)
            is SupportedTransportClient.Http -> client.client.setResource(resource)
        }
    }

    public fun shutdownWithTimeout(timeout: Duration): Result<Unit> =
        when (client) {
            is SupportedTransportClient.Tonic -> client.client.shutdownWithTimeout(timeout)
            is SupportedTransportClient.Http -> client.client.shutdownWithTimeout(timeout)
        }

    public companion object {
        /** Obtain a builder to configure a [LogExporter]. */
        public fun builder(): LogExporterBuilder<NoExporterBuilderSet> =
            LogExporterBuilder.new()

        public fun fromHttp(client: OtlpHttpClient): LogExporter =
            LogExporter(SupportedTransportClient.Http(client))

        public fun fromTonic(client: TonicLogsClient): LogExporter =
            LogExporter(SupportedTransportClient.Tonic(client))
    }
}
