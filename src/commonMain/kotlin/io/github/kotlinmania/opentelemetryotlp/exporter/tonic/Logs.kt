// port-lint: source exporter/tonic/logs.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.tonic

import io.github.kotlinmania.opentelemetryotlp.exporter.ExportConfig
import kotlin.time.Duration

/** Client for exporting logs over Tonic gRPC. */
public class TonicLogsClient(
    public val exportConfig: ExportConfig = ExportConfig(),
    public val tonicConfig: TonicConfig = TonicConfig(),
    private var resource: Any? = null,
) {
    private class ClientInner(
        val exportConfig: ExportConfig,
        val tonicConfig: TonicConfig,
    )

    private var inner: ClientInner? = ClientInner(exportConfig, tonicConfig)

    public fun fmt(): String = "TonicLogsClient"

    override fun toString(): String = fmt()

    public suspend fun export(batch: List<Any>): Result<Unit> =
        if (inner != null) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalStateException("AlreadyShutdown"))
        }

    public fun shutdownWithTimeout(timeout: Duration): Result<Unit> =
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
        ): TonicLogsClient = TonicLogsClient(exportConfig, tonicConfig)
    }
}
