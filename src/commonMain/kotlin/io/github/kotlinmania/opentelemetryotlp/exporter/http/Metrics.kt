// port-lint: source exporter/http/metrics.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.http

/**
 * Export metrics using the HTTP client.
 */
public suspend fun OtlpHttpClient.export(metrics: Any): Result<Unit> =
    exportMetrics(metrics)

internal fun OtlpHttpClient.shutdownMetrics(): Result<Unit> =
    shutdown(this)

private fun shutdown(client: OtlpHttpClient): Result<Unit> =
    client.shutdown()
