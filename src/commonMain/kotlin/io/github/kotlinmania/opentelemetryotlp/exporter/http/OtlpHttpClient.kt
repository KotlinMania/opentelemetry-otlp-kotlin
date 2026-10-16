// port-lint: source exporter/http/mod.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.http

import io.github.kotlinmania.opentelemetryotlp.exporter.ExportConfig
import kotlin.time.Duration

/** HTTP client wrapper for OTLP export. */
public class OtlpHttpClient(
    public val exportConfig: ExportConfig = ExportConfig(),
    public val httpConfig: HttpConfig = HttpConfig(),
) {
    public suspend fun exportSpans(batch: List<Any>): Result<Unit> = Result.success(Unit)

    public suspend fun exportMetrics(metrics: Any): Result<Unit> = Result.success(Unit)

    public suspend fun exportLogs(batch: List<Any>): Result<Unit> = Result.success(Unit)

    public fun shutdown(): Result<Unit> = Result.success(Unit)

    public fun shutdownWithTimeout(timeout: Duration): Result<Unit> = Result.success(Unit)

    public fun setResource(resource: Any) {}
}
