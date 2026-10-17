import Testing
import OpentelemetryOtlp

// Smoke test for the Kotlin → Swift Export → SPM → swift test pipeline.
@Suite("OpentelemetryOtlp Export Tests")
struct OpentelemetryOtlpExportTests {
    @Test("Swift module loads and imports cleanly")
    func swiftModuleLoads() {
        #expect(Bool(true))
    }

    @Test("Protocol, Compression, Temporality, and ExportConfig interoperate cleanly")
    func exportedTypesWork() {
        let config = exporter.ExportConfig(endpoint: "http://localhost:4318", protocol: OtlpProtocol.HttpBinary, timeout: nil)
        #expect(config.endpoint == "http://localhost:4318")
        #expect(config.protocol == OtlpProtocol.HttpBinary)

        config.protocol = OtlpProtocol.Grpc
        #expect(config.protocol == OtlpProtocol.Grpc)

        #expect(exporter.Compression.Gzip.toString() == "gzip")
        #expect(exporter.Compression.Zstd.toString() == "zstd")

        #expect(Temporality.Cumulative != Temporality.Delta)
        #expect(Temporality.Delta != Temporality.LowMemory)

        let noSet = NoExporterBuilderSet()
        #expect(noSet.toString() == "NoExporterBuilderSet")
    }
}

