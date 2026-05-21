// port-lint: ignore
// Kotlin-side tests exercising the leaf helpers translated from
// `src/exporter/mod.rs`. Upstream tests there rely on `temp_env` and on
// builder types that have not been ported yet, so this file is original
// Kotlin verification, not a 1:1 translation.
package io.github.kotlinmania.opentelemetryotlp.exporter

import io.github.kotlinmania.opentelemetryotlp.Protocol
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class ModTest {

    @Test
    fun protocolDefaultIsHttpBinary() {
        assertEquals(Protocol.HttpBinary, defaultProtocol())
    }

    @Test
    fun exportConfigDefaultsMatchUpstream() {
        val cfg = ExportConfig()
        assertNull(cfg.endpoint)
        assertEquals(Protocol.HttpBinary, cfg.protocol)
        assertNull(cfg.timeout)
    }

    @Test
    fun timeoutDefaultIs10Seconds() {
        assertEquals(10.seconds, OTEL_EXPORTER_OTLP_TIMEOUT_DEFAULT)
        assertEquals(10_000.milliseconds, OTEL_EXPORTER_OTLP_TIMEOUT_DEFAULT)
    }

    @Test
    fun compressionToString() {
        assertEquals("gzip", Compression.Gzip.toString())
        assertEquals("zstd", Compression.Zstd.toString())
    }

    @Test
    fun compressionFromStringAcceptsKnownAlgorithms() {
        assertEquals(Compression.Gzip, Compression.fromString("gzip").getOrThrow())
        assertEquals(Compression.Zstd, Compression.fromString("zstd").getOrThrow())
    }

    @Test
    fun compressionFromStringRejectsUnknown() {
        val result = Compression.fromString("snappy")
        assertTrue(result.isFailure)
        val err = result.exceptionOrNull()
        assertTrue(err is ExporterBuildError.UnsupportedCompressionAlgorithm)
        assertEquals("snappy", err.algorithm)
    }

    @Test
    fun urlDecodePassThroughPlainAscii() {
        assertEquals("hello", urlDecode("hello"))
    }

    @Test
    fun urlDecodePercentEscape() {
        assertEquals("a b", urlDecode("a%20b"))
        assertEquals("a/b", urlDecode("a%2Fb"))
    }

    @Test
    fun urlDecodeRejectsTruncatedEscape() {
        assertNull(urlDecode("a%2"))
        assertNull(urlDecode("a%"))
    }

    @Test
    fun urlDecodeRejectsNonHexEscape() {
        assertNull(urlDecode("a%zz"))
    }

    @Test
    fun parseHeaderKeyValueStringTrimsBothSides() {
        val (k, v) = assertNotNull(parseHeaderKeyValueString("  k =  v "))
        assertEquals("k", k)
        assertEquals("v", v)
    }

    @Test
    fun parseHeaderKeyValueStringDecodesValue() {
        val (k, v) = assertNotNull(parseHeaderKeyValueString("auth=Bearer%20token"))
        assertEquals("auth", k)
        assertEquals("Bearer token", v)
    }

    @Test
    fun parseHeaderKeyValueStringRejectsMissingDelimiter() {
        assertNull(parseHeaderKeyValueString("nokey"))
    }

    @Test
    fun parseHeaderKeyValueStringRejectsEmptyKey() {
        assertNull(parseHeaderKeyValueString("=v"))
    }

    @Test
    fun parseHeaderKeyValueStringRejectsEmptyValue() {
        assertNull(parseHeaderKeyValueString("k="))
    }

    @Test
    fun parseHeaderStringCommaSeparated() {
        val pairs = parseHeaderString("a=1,b=2 , c=3").toList()
        assertEquals(listOf("a" to "1", "b" to "2", "c" to "3"), pairs)
    }

    @Test
    fun parseHeaderStringSkipsEmptySegments() {
        val pairs = parseHeaderString("a=1,,b=2").toList()
        assertEquals(listOf("a" to "1", "b" to "2"), pairs)
    }

    @Test
    fun applyExportConfigCopiesAllFields() {
        val target = object : HasExportConfig {
            override var exportConfig: ExportConfig = ExportConfig()
        }
        val source = ExportConfig(
            endpoint = "http://example.invalid",
            protocol = Protocol.Grpc,
            timeout = 5.seconds,
        )
        target.applyExportConfig(source)
        assertEquals("http://example.invalid", target.exportConfig.endpoint)
        assertEquals(Protocol.Grpc, target.exportConfig.protocol)
        assertEquals(5.seconds, target.exportConfig.timeout)
    }

    @Test
    fun applyEndpointChainsAndReturnsReceiver() {
        val builder = object : HasExportConfig {
            override var exportConfig: ExportConfig = ExportConfig()
        }
        val returned = builder.applyEndpoint("http://otel:4318").applyTimeout(7.seconds)
        assertEquals(builder, returned)
        assertEquals("http://otel:4318", builder.exportConfig.endpoint)
        assertEquals(7.seconds, builder.exportConfig.timeout)
    }

    @Test
    fun defaultHeadersIncludesUserAgent() {
        val headers = defaultHeaders()
        val userAgent = headers["User-Agent"]
        assertNotNull(userAgent)
        assertTrue(userAgent.startsWith("OTel-OTLP-Exporter-Rust/"))
    }
}
