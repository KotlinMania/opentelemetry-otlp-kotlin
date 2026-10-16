// port-lint: tests exporter/mod.rs
package io.github.kotlinmania.opentelemetryotlp.exporter

import io.github.kotlinmania.opentelemetryotlp.Protocol
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class ExportConfigTest {
    @Test
    fun testDefaultProtocol() {
        assertEquals(Protocol.HttpBinary, defaultProtocol())
    }

    @Test
    fun testDefaultHttpEndpoint() {
        val cfg = ExportConfig()
        assertNull(cfg.endpoint)
    }

    @Test
    fun testDefaultTonicEndpoint() {
        val cfg = ExportConfig()
        assertNull(cfg.endpoint)
    }

    @Test
    fun testTimeoutDefault() {
        assertEquals(10.seconds, OTEL_EXPORTER_OTLP_TIMEOUT_DEFAULT)
        assertEquals(10_000.milliseconds, OTEL_EXPORTER_OTLP_TIMEOUT_DEFAULT)
    }

    @Test
    fun testCompressionToString() {
        assertEquals("gzip", Compression.Gzip.toString())
        assertEquals("zstd", Compression.Zstd.toString())
    }

    @Test
    fun testCompressionFromString() {
        assertEquals(Compression.Gzip, Compression.fromString("gzip").getOrThrow())
        assertEquals(Compression.Zstd, Compression.fromString("zstd").getOrThrow())
        val result = Compression.fromString("snappy")
        assertTrue(result.isFailure)
        val err = result.exceptionOrNull()
        assertTrue(err is ExporterBuildError.UnsupportedCompressionAlgorithm)
        assertEquals("snappy", err.algorithm)
    }

    @Test
    fun testUrlDecode() {
        assertEquals("hello", urlDecode("hello"))
        assertEquals("v 1", urlDecode("v%201"))
        assertEquals("v 1", urlDecode("v 1"))
        assertEquals("öà§ÖabcdÄ", urlDecode("%C3%B6%C3%A0%C2%A7%C3%96abcd%C3%84"))
        assertNull(urlDecode("v%XX1"))
        assertNull(urlDecode("a%2"))
        assertNull(urlDecode("a%"))
    }

    @Test
    fun testParseHeaderString() {
        val r1 = parseHeaderString("k1=v1").toList()
        assertEquals(listOf("k1" to "v1"), r1)

        val r2 = parseHeaderString("k1=v1,k2=v2").toList()
        assertEquals(listOf("k1" to "v1", "k2" to "v2"), r2)

        val r3 = parseHeaderString("k1=v1=10,k2,k3").toList()
        assertEquals(listOf("k1" to "v1=10"), r3)

        val r4 = parseHeaderString("k1=v1,,,k2,k3=10").toList()
        assertEquals(listOf("k1" to "v1", "k3" to "10"), r4)
    }

    @Test
    fun testParseHeaderKeyValueString() {
        assertEquals("k1" to "v1", parseHeaderKeyValueString("k1=v1"))
        assertEquals("Authentication" to "Basic AAA", parseHeaderKeyValueString("Authentication=Basic AAA"))
        assertEquals("Authentication" to "Basic AAA", parseHeaderKeyValueString("Authentication=Basic%20AAA"))
        assertEquals("k1" to "%XX", parseHeaderKeyValueString("k1=%XX"))
        assertNull(parseHeaderKeyValueString(""))
        assertNull(parseHeaderKeyValueString("=v1"))
        assertNull(parseHeaderKeyValueString("k1="))
        assertNull(parseHeaderKeyValueString("nokey"))
    }

    @Test
    fun testApplyExportConfig() {
        val cfg = ExportConfig(
            endpoint = "http://localhost:4318",
            protocol = Protocol.HttpJson,
            timeout = 5.seconds,
        )
        val target = object : HasExportConfig {
            override var exportConfig: ExportConfig = ExportConfig()
        }
        target.applyExportConfig(cfg)
        assertEquals("http://localhost:4318", target.exportConfig.endpoint)
        assertEquals(Protocol.HttpJson, target.exportConfig.protocol)
        assertEquals(5.seconds, target.exportConfig.timeout)
    }

    @Test
    fun testDefaultHeaders() {
        val headers = defaultHeaders()
        assertTrue(headers.containsKey("User-Agent"))
        assertTrue(headers["User-Agent"]!!.startsWith("OTel-OTLP-Exporter-Rust/"))
    }
}
