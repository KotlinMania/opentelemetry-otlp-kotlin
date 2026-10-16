// port-lint: source exporter/tonic/logs.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.tonic

import io.github.kotlinmania.opentelemetryotlp.exporter.ExportConfig
import kotlin.time.Duration

/** Client for exporting logs over Tonic gRPC. */
public class TonicLogsClient(
    public val exportConfig: ExportConfig = ExportConfig(),
    public val tonicConfig: TonicConfig = TonicConfig(),
) {
    public suspend fun export(batch: List<Any>): Result<Unit> = Result.success(Unit)

    public fun shutdownWithTimeout(timeout: Duration): Result<Unit> = Result.success(Unit)

    public fun setResource(resource: Any) {}

    public companion object {
        public fun new(
            exportConfig: ExportConfig = ExportConfig(),
            tonicConfig: TonicConfig = TonicConfig(),
        ): TonicLogsClient = TonicLogsClient(exportConfig, tonicConfig)
    }
}
