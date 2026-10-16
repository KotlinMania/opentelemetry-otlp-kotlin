// port-lint: source exporter/tonic/mod.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.tonic

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
 * Configuration builder for the OTLP Tonic exporter.
 */
public class TonicExporterBuilder(
    override var exportConfig: ExportConfig = ExportConfig(),
    override var tonicConfig: TonicConfig = TonicConfig(),
) : HasExportConfig,
    HasTonicConfig,
    WithExportConfig<TonicExporterBuilder>,
    WithTonicConfig<TonicExporterBuilder> {
    override fun withEndpoint(endpoint: String): TonicExporterBuilder {
        exportConfig.endpoint = endpoint
        return this
    }

    override fun withProtocol(protocol: Protocol): TonicExporterBuilder {
        exportConfig.protocol = protocol
        return this
    }

    override fun withTimeout(timeout: kotlin.time.Duration): TonicExporterBuilder {
        exportConfig.timeout = timeout
        return this
    }

    override fun withExportConfig(exportConfig: ExportConfig): TonicExporterBuilder {
        this.exportConfig = exportConfig
        return this
    }

    override fun withMetadata(metadata: Map<String, String>): TonicExporterBuilder {
        val current = (tonicConfig.metadata ?: emptyMap()).toMutableMap()
        current.putAll(metadata)
        tonicConfig.metadata = current
        return this
    }

    override fun withCompression(compression: Compression): TonicExporterBuilder {
        tonicConfig.compression = compression
        return this
    }

    public fun buildSpanExporter(): Result<SpanExporter> =
        Result.success(SpanExporter.fromTonic(TonicTracesClient(exportConfig, tonicConfig)))

    public fun buildLogExporter(): Result<LogExporter> =
        Result.success(LogExporter.fromTonic(TonicLogsClient(exportConfig, tonicConfig)))

    public fun buildMetricsExporter(temporality: Temporality = Temporality.Cumulative): Result<MetricExporter> =
        Result.success(MetricExporter.fromTonic(TonicMetricsClient(exportConfig, tonicConfig), temporality))
}
