// port-lint: source lib.rs
package io.github.kotlinmania.opentelemetryotlp

// Tracking file for upstream lib.rs.
//
// Upstream module declarations:
//   Module `exporter` (parceled into io.github.kotlinmania.opentelemetryotlp.exporter)
//   Module `logs` (parceled into Logs.kt)
//   Module `metric` (parceled into Metric.kt)
//   Module `span` (parceled into Span.kt)
//   Module `tonic_types`
//
// Upstream symbols parceled:
//   NoExporterBuilderSet -> BuilderSets.kt
//   TonicExporterBuilderSet -> BuilderSets.kt
//   HttpExporterBuilderSet -> BuilderSets.kt
//   Protocol -> Protocol.kt
//   NoExporterConfig -> NoExporterConfig.kt
//
// Upstream re-exports:
//   Re-exports Compression, ExportConfig, ExporterBuildError from exporter
//   Re-exports SpanExporter, SpanExporterBuilder, constants from span
//   Re-exports MetricExporter, MetricExporterBuilder, constants from metric
//   Re-exports LogExporter, LogExporterBuilder, constants from logs
//   Re-exports HasHttpConfig, WithHttpConfig, HttpExporterBuilder from exporter.http
//   Re-exports HasTonicConfig, WithTonicConfig, TonicConfig, TonicExporterBuilder from exporter.tonic
//   Re-exports HasExportConfig, WithExportConfig, constants from exporter
//
// Callers migrated:
// (none)
