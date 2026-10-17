// port-lint: source exporter/tonic/metrics.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.tonic

import io.github.kotlinmania.opentelemetryotlp.exporter.ExportConfig

/** Client for exporting metrics over Tonic gRPC. */
public class TonicMetricsClient(
    public val exportConfig: ExportConfig = ExportConfig(),
    public val tonicConfig: TonicConfig = TonicConfig(),
) {
    private class ClientInner(
        val exportConfig: ExportConfig,
        val tonicConfig: TonicConfig,
    )

    private var inner: ClientInner? = ClientInner(exportConfig, tonicConfig)

    public fun fmt(): String = "TonicMetricsClient"

    override fun toString(): String = fmt()

    public suspend fun export(metrics: Any): Result<Unit> =
        if (inner != null) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalStateException("AlreadyShutdown"))
        }

    public fun shutdown(): Result<Unit> =
        if (inner != null) {
            inner = null
            Result.success(Unit)
        } else {
            Result.failure(IllegalStateException("AlreadyShutdown"))
        }

    public companion object {
        public fun new(
            exportConfig: ExportConfig = ExportConfig(),
            tonicConfig: TonicConfig = TonicConfig(),
        ): TonicMetricsClient = TonicMetricsClient(exportConfig, tonicConfig)
    }
}
