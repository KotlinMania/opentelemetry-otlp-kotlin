package io.github.kotlinmania.opentelemetryotlp.exporter.tonic

import io.github.kotlinmania.opentelemetryotlp.exporter.Compression
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TonicConfigTest {
    @Test
    fun testWithMetadata() {
        val metadata = mapOf("foo" to "bar")
        val builder = TonicExporterBuilder().withMetadata(metadata)
        val result = builder.tonicConfig.metadata
        assertNotNull(result)
        assertEquals("bar", result["foo"])

        val overrideMetadata = mapOf("foo" to "baz")
        val builder2 = builder.withMetadata(overrideMetadata)
        val result2 = builder2.tonicConfig.metadata
        assertNotNull(result2)
        assertEquals("baz", result2["foo"])
    }

    @Test
    fun testWithGzipCompression() {
        val builder = TonicExporterBuilder().withCompression(Compression.Gzip)
        assertEquals(Compression.Gzip, builder.tonicConfig.compression)
    }

    @Test
    fun testWithZstdCompression() {
        val builder = TonicExporterBuilder().withCompression(Compression.Zstd)
        assertEquals(Compression.Zstd, builder.tonicConfig.compression)
    }
}
