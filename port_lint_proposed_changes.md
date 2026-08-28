# port-lint Proposed Changes

**Generated:** 2026-08-28
**Source:** tmp/opentelemetry-otlp/src
**Target:** src/commonMain/kotlin/io/github/kotlinmania/opentelemetryotlp

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonMain/kotlin/io/github/kotlinmania/opentelemetryotlp/exporter/Mod.kt` | `// port-lint: source src/exporter/mod.rs` | `// port-lint: source exporter/mod.rs` | `exporter/mod.rs` | `port-lint provenance header matched only after fallback normalization: 'src/exporter/mod.rs' vs expected 'exporter/mod.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/opentelemetryotlp/exporter/ModTest.kt` | `// port-lint: tests src/exporter/mod.rs` | `// port-lint: tests exporter/mod.rs` | `exporter/mod.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:src/exporter/mod.rs' vs expected 'exporter/mod.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/opentelemetryotlp/Lib.kt` | `// port-lint: source src/lib.rs` | `// port-lint: source lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'src/lib.rs' vs expected 'lib.rs'` |
