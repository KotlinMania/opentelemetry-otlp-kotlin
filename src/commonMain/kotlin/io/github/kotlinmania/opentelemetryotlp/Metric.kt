// port-lint: source metric.rs
package io.github.kotlinmania.opentelemetryotlp

import io.github.kotlinmania.opentelemetryotlp.exporter.ExportConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.http.HttpConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.http.OtlpHttpClient
import io.github.kotlinmania.opentelemetryotlp.exporter.tonic.TonicConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.tonic.TonicMetricsClient
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/** Target to which the exporter is going to send metrics, defaults to https://localhost:4317/v1/metrics. */
public const val OTEL_EXPORTER_OTLP_METRICS_ENDPOINT: String = "OTEL_EXPORTER_OTLP_METRICS_ENDPOINT"

/** Max waiting time for the backend to process each metrics batch, defaults to 10s. */
public const val OTEL_EXPORTER_OTLP_METRICS_TIMEOUT: String = "OTEL_EXPORTER_OTLP_METRICS_TIMEOUT"

/** Compression algorithm to use, defaults to none. */
public const val OTEL_EXPORTER_OTLP_METRICS_COMPRESSION: String = "OTEL_EXPORTER_OTLP_METRICS_COMPRESSION"

/** Key-value pairs to be used as headers associated with gRPC or HTTP requests for sending metrics. */
public const val OTEL_EXPORTER_OTLP_METRICS_HEADERS: String = "OTEL_EXPORTER_OTLP_METRICS_HEADERS"

/** Defines the window that an aggregation was calculated over. */
public enum class Temporality {
    Cumulative,
    Delta,
    LowMemory,
}

/** A builder for creating a new [MetricExporter]. */
public class MetricExporterBuilder<C>(
    public val client: C,
    public val temporality: Temporality = Temporality.Cumulative,
) {
    public fun withTonic(): MetricExporterBuilder<TonicExporterBuilderSet> =
        MetricExporterBuilder(TonicExporterBuilderSet(), temporality)

    public fun withHttp(): MetricExporterBuilder<HttpExporterBuilderSet> =
        MetricExporterBuilder(HttpExporterBuilderSet(), temporality)

    public fun withTemporality(temporality: Temporality): MetricExporterBuilder<C> =
        MetricExporterBuilder(client, temporality)

    public companion object {
        /** Create a new [MetricExporterBuilder] with default settings. */
        public fun new(): MetricExporterBuilder<NoExporterBuilderSet> =
            MetricExporterBuilder(NoExporterBuilderSet())
    }
}

/** Build the [MetricExporter] with the gRPC Tonic transport. */
@kotlin.jvm.JvmName("buildTonic")
public fun MetricExporterBuilder<TonicExporterBuilderSet>.build(): Result<MetricExporter> =
    client.builder.buildMetricsExporter(temporality)

/** Build the [MetricExporter] with the HTTP transport. */
@kotlin.jvm.JvmName("buildHttp")
public fun MetricExporterBuilder<HttpExporterBuilderSet>.build(): Result<MetricExporter> =
    client.builder.buildMetricsExporter(temporality)

/** Return export config for Tonic builder. */
@kotlin.jvm.JvmName("exportConfigTonic")
public fun MetricExporterBuilder<TonicExporterBuilderSet>.exportConfig(): ExportConfig =
    client.builder.exportConfig

/** Return export config for HTTP builder. */
@kotlin.jvm.JvmName("exportConfigHttp")
public fun MetricExporterBuilder<HttpExporterBuilderSet>.exportConfig(): ExportConfig =
    client.builder.exportConfig

/** Return Tonic config. */
public fun MetricExporterBuilder<TonicExporterBuilderSet>.tonicConfig(): TonicConfig =
    client.builder.tonicConfig

/** Return HTTP client config. */
public fun MetricExporterBuilder<HttpExporterBuilderSet>.httpClientConfig(): HttpConfig =
    client.builder.httpConfig

/** An interface for OTLP metrics clients. */
public interface MetricsClient {
    public suspend fun export(metrics: Any): Result<Unit>

    public fun shutdown(): Result<Unit>
}

/** Export metrics in OTEL format. */
public class MetricExporter internal constructor(
    public val client: SupportedTransportClient,
    internal val temporality: Temporality,
) {
    public sealed class SupportedTransportClient {
        public class Tonic(
            public val client: TonicMetricsClient,
        ) : SupportedTransportClient()

        public class Http(
            public val client: OtlpHttpClient,
        ) : SupportedTransportClient()
    }

    public fun fmt(): String = "MetricExporter"

    override fun toString(): String = fmt()

    public suspend fun export(metrics: Any): Result<Unit> =
        when (client) {
            is SupportedTransportClient.Tonic -> client.client.export(metrics)
            is SupportedTransportClient.Http -> client.client.exportMetrics(metrics)
        }

    public fun forceFlush(): Result<Unit> = Result.success(Unit)

    public fun shutdown(): Result<Unit> = shutdownWithTimeout(5.seconds)

    public fun shutdownWithTimeout(timeout: Duration): Result<Unit> =
        when (client) {
            is SupportedTransportClient.Tonic -> client.client.shutdown()
            is SupportedTransportClient.Http -> client.client.shutdown()
        }

    public fun temporality(): Temporality = temporality

    public companion object {
        /** Obtain a builder to configure a [MetricExporter]. */
        public fun builder(): MetricExporterBuilder<NoExporterBuilderSet> =
            MetricExporterBuilder.new()

        public fun fromTonic(
            client: TonicMetricsClient,
            temporality: Temporality = Temporality.Cumulative,
        ): MetricExporter =
            MetricExporter(SupportedTransportClient.Tonic(client), temporality)

        public fun fromHttp(
            client: OtlpHttpClient,
            temporality: Temporality = Temporality.Cumulative,
        ): MetricExporter =
            MetricExporter(SupportedTransportClient.Http(client), temporality)
    }
}
