// port-lint: tests tests/smoke.rs
package io.github.kotlinmania.opentelemetryotlp

import io.github.kotlinmania.opentelemetryotlp.exporter.Compression
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SmokeTest {
    @Test
    fun smokeTracer() {
        val metadata = mapOf("x-header-key" to "header-value")
        val exporterResult =
            SpanExporter
                .builder()
                .withTonic()
                .withCompression(Compression.Gzip)
                .withEndpoint("http://localhost:4317")
                .withMetadata(metadata)
                .build()

        assertTrue(exporterResult.isSuccess)
        val exporter = exporterResult.getOrThrow()
        assertNotNull(exporter)
        val shutdownResult = exporter.shutdown()
        assertTrue(shutdownResult.isSuccess)
    }
}
