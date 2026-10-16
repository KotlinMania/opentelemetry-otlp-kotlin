// port-lint: source exporter/http/mod.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.http

import io.github.kotlinmania.opentelemetryotlp.LogExporter
import io.github.kotlinmania.opentelemetryotlp.MetricExporter
import io.github.kotlinmania.opentelemetryotlp.Protocol
import io.github.kotlinmania.opentelemetryotlp.SpanExporter
import io.github.kotlinmania.opentelemetryotlp.Temporality
import io.github.kotlinmania.opentelemetryotlp.exporter.Compression
import io.github.kotlinmania.opentelemetryotlp.exporter.ExportConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.HasExportConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.WithExportConfig

/**
 * Configuration builder for the OTLP HTTP exporter.
 */
public class HttpExporterBuilder(
    override var exportConfig: ExportConfig = ExportConfig(),
    public var httpConfig: HttpConfig = HttpConfig(),
) : HasExportConfig, HasHttpConfig, WithExportConfig<HttpExporterBuilder>, WithHttpConfig<HttpExporterBuilder> {

    override fun httpClientConfig(): HttpConfig = httpConfig

    override fun withEndpoint(endpoint: String): HttpExporterBuilder {
        exportConfig.endpoint = endpoint
        return this
    }

    override fun withProtocol(protocol: Protocol): HttpExporterBuilder {
        exportConfig.protocol = protocol
        return this
    }

    override fun withTimeout(timeout: kotlin.time.Duration): HttpExporterBuilder {
        exportConfig.timeout = timeout
        return this
    }

    override fun withExportConfig(exportConfig: ExportConfig): HttpExporterBuilder {
        this.exportConfig = exportConfig
        return this
    }

    override fun withHeaders(headers: Map<String, String>): HttpExporterBuilder {
        val current = (httpConfig.headers ?: emptyMap()).toMutableMap()
        current.putAll(headers)
        httpConfig.headers = current
        return this
    }

    override fun withCompression(compression: Compression): HttpExporterBuilder {
        httpConfig.compression = compression
        return this
    }

    public fun buildSpanExporter(): Result<SpanExporter> =
        Result.success(SpanExporter.fromHttp(OtlpHttpClient(exportConfig, httpConfig)))

    public fun buildLogExporter(): Result<LogExporter> =
        Result.success(LogExporter.fromHttp(OtlpHttpClient(exportConfig, httpConfig)))

    public fun buildMetricsExporter(temporality: Temporality = Temporality.Cumulative): Result<MetricExporter> =
        Result.success(MetricExporter.fromHttp(OtlpHttpClient(exportConfig, httpConfig), temporality))
}
