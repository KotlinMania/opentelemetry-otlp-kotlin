// port-lint: source exporter/http/trace.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.http

/**
 * Export tracing spans using the HTTP client.
 */
public suspend fun OtlpHttpClient.exportTrace(batch: List<Any>): Result<Unit> =
    exportSpans(batch)
