// port-lint: source exporter/http/trace.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.http

/**
 * Export tracing spans using the HTTP client.
 */
public suspend fun OtlpHttpClient.exportTrace(batch: List<Any>): Result<Unit> =
    export(this, batch)

private suspend fun export(
    client: OtlpHttpClient,
    batch: List<Any>,
): Result<Unit> = client.exportSpans(batch)

internal fun OtlpHttpClient.shutdownTrace(): Result<Unit> =
    shutdown(this)

private fun shutdown(client: OtlpHttpClient): Result<Unit> =
    client.shutdown()

internal fun OtlpHttpClient.setTraceResource(resource: Any) {
    setResource(this, resource)
}

private fun setResource(
    client: OtlpHttpClient,
    resource: Any,
) {
    client.setResource(resource)
}
