import Testing
import OpentelemetryOtlp

// Smoke test for the Kotlin → Swift Export → SPM → swift test pipeline.
@Suite("OpentelemetryOtlp Export Tests")
struct OpentelemetryOtlpExportTests {
    @Test("Swift module loads and imports cleanly")
    func swiftModuleLoads() {
        #expect(true)
    }
}

