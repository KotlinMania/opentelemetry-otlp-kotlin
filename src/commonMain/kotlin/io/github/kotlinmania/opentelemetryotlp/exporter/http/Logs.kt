// port-lint: source exporter/http/logs.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.http

/**
 * Export logs using the HTTP client.
 */
public suspend fun OtlpHttpClient.exportLogsBatch(batch: List<Any>): Result<Unit> =
    exportLogs(batch)
