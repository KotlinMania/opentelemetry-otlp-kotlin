// port-lint: tests exporter/http/mod.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.http

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HttpConfigTest {
    @Test
    fun testBuildEndpointUri() {
        val uri = buildEndpointUri("https://example.com", "/v1/traces").getOrThrow()
        assertEquals("https://example.com/v1/traces", uri)

        // Should be no duplicate slashes:
        val uri2 = buildEndpointUri("https://example.com/", "/v1/traces").getOrThrow()
        assertEquals("https://example.com/v1/traces", uri2)

        // Append paths properly:
        val uri3 = buildEndpointUri("https://example.com/additional/path/", "/v1/traces").getOrThrow()
        assertEquals("https://example.com/additional/path/v1/traces", uri3)
    }

    @Test
    fun testAllInvalidUrlsFallsBackToError() {
        val result =
            resolveHttpEndpoint(
                "OTEL_EXPORTER_OTLP_TRACES_ENDPOINT",
                "/v1/traces",
                "-*/*-/*-//-/-/yet-another-invalid-uri",
            )
        assertTrue(result.isFailure)
    }

    @Test
    fun testAddHeaderFromString() {
        val testCases =
            listOf(
                "k1=v1" to mapOf("k1" to "v1"),
                "k1=v1,k2=v2" to mapOf("k1" to "v1", "k2" to "v2"),
                "k1=v1=10,k2,k3" to mapOf("k1" to "v1=10"),
                "k1=v1,,,k2,k3=10" to mapOf("k1" to "v1", "k3" to "10"),
            )

        for ((inputStr, expectedHeaders) in testCases) {
            val headers = mutableMapOf<String, String>()
            addHeaderFromString(inputStr, headers)
            assertEquals(expectedHeaders.size, headers.size, "Failed on input: $inputStr")
            for ((expectedKey, expectedValue) in expectedHeaders) {
                assertEquals(expectedValue, headers[expectedKey], "Failed on key: $expectedKey with input: $inputStr")
            }
        }
    }

    @Test
    fun testHttpExporterBuilderWithHeaders() {
        val initialHeaders = mutableMapOf("k1" to "v1")
        val extraHeaders = mapOf("k2" to "v2", "k3" to "v3")
        val expectedHeaders = mapOf("k1" to "v1", "k2" to "v2", "k3" to "v3")

        val builder =
            HttpExporterBuilder(
                httpConfig = HttpConfig(headers = initialHeaders),
            )

        val updated = builder.withHeaders(extraHeaders)
        assertEquals(expectedHeaders, updated.httpConfig.headers)
    }

    @Test
    fun testHttpExporterEndpoint() {
        val exporter = HttpExporterBuilder()
        val url =
            resolveHttpEndpoint(
                "OTEL_EXPORTER_OTLP_TRACES_ENDPOINT",
                "/v1/traces",
                exporter.exportConfig.endpoint,
            ).getOrThrow()
        assertEquals("http://localhost:4318/v1/traces", url)

        val customExporter =
            HttpExporterBuilder()
                .withEndpoint("http://localhost:4318/v1/tracesbutnotreally")
        val customUrl =
            resolveHttpEndpoint(
                "OTEL_EXPORTER_OTLP_TRACES_ENDPOINT",
                "/v1/traces",
                customExporter.exportConfig.endpoint,
            ).getOrThrow()
        assertEquals("http://localhost:4318/v1/tracesbutnotreally", customUrl)
    }
}
