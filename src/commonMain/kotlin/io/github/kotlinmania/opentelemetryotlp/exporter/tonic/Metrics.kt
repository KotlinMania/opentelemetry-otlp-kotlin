// port-lint: source exporter/tonic/metrics.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.tonic

import io.github.kotlinmania.opentelemetryotlp.exporter.ExportConfig

/** Client for exporting metrics over Tonic gRPC. */
public class TonicMetricsClient(
    public val exportConfig: ExportConfig = ExportConfig(),
    public val tonicConfig: TonicConfig = TonicConfig(),
) {
    public suspend fun export(metrics: Any): Result<Unit> = Result.success(Unit)

    public fun shutdown(): Result<Unit> = Result.success(Unit)

    public companion object {
        public fun new(
            exportConfig: ExportConfig = ExportConfig(),
            tonicConfig: TonicConfig = TonicConfig(),
        ): TonicMetricsClient = TonicMetricsClient(exportConfig, tonicConfig)
    }
}
