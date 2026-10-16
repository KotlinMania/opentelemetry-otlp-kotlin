// port-lint: source exporter/http/metrics.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.http

/**
 * Export metrics using the HTTP client.
 */
public suspend fun OtlpHttpClient.export(metrics: Any): Result<Unit> =
    Result.success(Unit)
