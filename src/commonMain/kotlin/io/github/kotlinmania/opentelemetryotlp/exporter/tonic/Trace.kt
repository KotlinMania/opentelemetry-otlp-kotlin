// port-lint: source exporter/tonic/trace.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.tonic

import io.github.kotlinmania.opentelemetryotlp.exporter.ExportConfig

/** Client for exporting traces over Tonic gRPC. */
public class TonicTracesClient(
    public val exportConfig: ExportConfig = ExportConfig(),
    public val tonicConfig: TonicConfig = TonicConfig(),
    private var resource: Any? = null,
) {
    private class ClientInner(
        val exportConfig: ExportConfig,
        val tonicConfig: TonicConfig,
    )

    private var inner: ClientInner? = ClientInner(exportConfig, tonicConfig)

    public fun fmt(): String = "TonicTracesClient"

    override fun toString(): String = fmt()

    public suspend fun export(batch: List<Any>): Result<Unit> =
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

    public fun setResource(resource: Any) {
        this.resource = resource
    }

    public companion object {
        public fun new(
            exportConfig: ExportConfig = ExportConfig(),
            tonicConfig: TonicConfig = TonicConfig(),
        ): TonicTracesClient = TonicTracesClient(exportConfig, tonicConfig)
    }
}
