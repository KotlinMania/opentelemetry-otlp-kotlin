// port-lint: source exporter/tonic/trace.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.tonic

import io.github.kotlinmania.opentelemetryotlp.exporter.ExportConfig

/** Client for exporting traces over Tonic gRPC. */
public class TonicTracesClient(
    public val exportConfig: ExportConfig = ExportConfig(),
    public val tonicConfig: TonicConfig = TonicConfig(),
) {
    public suspend fun export(batch: List<Any>): Result<Unit> = Result.success(Unit)

    public fun shutdown(): Result<Unit> = Result.success(Unit)

    public fun setResource(resource: Any) {}

    public companion object {
        public fun new(
            exportConfig: ExportConfig = ExportConfig(),
            tonicConfig: TonicConfig = TonicConfig(),
        ): TonicTracesClient = TonicTracesClient(exportConfig, tonicConfig)
    }
}
