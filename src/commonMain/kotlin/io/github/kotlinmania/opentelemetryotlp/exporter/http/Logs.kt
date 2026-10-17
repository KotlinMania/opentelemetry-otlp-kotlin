// port-lint: source exporter/http/logs.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.http

import kotlin.time.Duration

/**
 * Export logs using the HTTP client.
 */
public suspend fun OtlpHttpClient.exportLogsBatch(batch: List<Any>): Result<Unit> =
    export(this, batch)

private suspend fun export(
    client: OtlpHttpClient,
    batch: List<Any>,
): Result<Unit> = client.exportLogs(batch)

internal fun OtlpHttpClient.shutdownLogsWithTimeout(timeout: Duration): Result<Unit> =
    shutdownWithTimeout(this, timeout)

private fun shutdownWithTimeout(
    client: OtlpHttpClient,
    timeout: Duration,
): Result<Unit> = client.shutdownWithTimeout(timeout)

internal fun OtlpHttpClient.setLogsResource(resource: Any) {
    setResource(this, resource)
}

private fun setResource(
    client: OtlpHttpClient,
    resource: Any,
) {
    client.setResource(resource)
}
