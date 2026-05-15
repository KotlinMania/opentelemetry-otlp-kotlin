// port-lint: source src/lib.rs
package io.github.kotlinmania.opentelemetryotlp

/**
 * # OpenTelemetry OTLP Exporter
 *
 * The OTLP Exporter enables exporting telemetry data (logs, metrics, and traces) in the
 * OpenTelemetry Protocol (OTLP) format to compatible backends. These backends include:
 *
 * - OpenTelemetry Collector
 * - Open-source observability tools (Prometheus, Jaeger, etc.)
 * - Vendor-specific monitoring platforms
 *
 * This crate supports sending OTLP data via:
 * - gRPC
 * - HTTP (binary protobuf or JSON)
 *
 * ## Quickstart with OpenTelemetry Collector
 *
 * ### HTTP Transport (Port 4318)
 *
 * Run the OpenTelemetry Collector:
 *
 * ```shell
 * $ docker run -p 4318:4318 otel/opentelemetry-collector:latest
 * ```
 *
 * Configure your application to export traces via HTTP:
 *
 * ```
 * // Initialize OTLP exporter using HTTP binary protocol
 * val otlpExporter = SpanExporter.builder()
 *     .withHttp()
 *     .withProtocol(Protocol.HttpBinary)
 *     .build()
 *
 * // Create a tracer provider with the exporter
 * val tracerProvider = SdkTracerProvider.builder()
 *     .withBatchExporter(otlpExporter)
 *     .build()
 *
 * // Set it as the global provider
 * Global.setTracerProvider(tracerProvider)
 *
 * // Get a tracer and create spans
 * val tracer = Global.tracer("my_tracer")
 * tracer.inSpan("doing_work") { _ ->
 *     // Your application logic here...
 * }
 * ```
 *
 * ### gRPC Transport (Port 4317)
 *
 * Run the OpenTelemetry Collector:
 *
 * ```shell
 * $ docker run -p 4317:4317 otel/opentelemetry-collector:latest
 * ```
 *
 * Configure your application to export traces via gRPC:
 *
 * ```
 * // Initialize OTLP exporter using gRPC (Tonic)
 * val otlpExporter = SpanExporter.builder()
 *     .withTonic()
 *     .build()
 *
 * // Create a tracer provider with the exporter
 * val tracerProvider = SdkTracerProvider.builder()
 *     .withBatchExporter(otlpExporter)
 *     .build()
 *
 * // Set it as the global provider
 * Global.setTracerProvider(tracerProvider)
 *
 * // Get a tracer and create spans
 * val tracer = Global.tracer("my_tracer")
 * tracer.inSpan("doing_work") { _ ->
 *     // Your application logic here...
 * }
 * ```
 *
 * ## Using with Jaeger
 *
 * Jaeger natively supports the OTLP protocol, making it easy to send traces directly:
 *
 * ```shell
 * $ docker run -p 16686:16686 -p 4317:4317 -e COLLECTOR_OTLP_ENABLED=true jaegertracing/all-in-one:latest
 * ```
 *
 * After running your application configured with the OTLP exporter, view traces at:
 * `http://localhost:16686`
 *
 * ## Using with Prometheus
 *
 * Prometheus natively supports accepting metrics via the OTLP protocol
 * (HTTP/protobuf). You can run Prometheus with the following command:
 *
 * ```shell
 * docker run -p 9090:9090 -v ./prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml --web.enable-otlp-receiver
 * ```
 *
 * (An empty prometheus.yml file is sufficient for this example.)
 *
 * Modify your application to export metrics via OTLP:
 *
 * ```
 * // Initialize OTLP exporter using HTTP binary protocol
 * val exporter = MetricExporter.builder()
 *     .withHttp()
 *     .withProtocol(Protocol.HttpBinary)
 *     .withEndpoint("http://localhost:9090/api/v1/otlp/v1/metrics")
 *     .build()
 *
 * // Create a meter provider with the OTLP Metric exporter
 * val meterProvider = SdkMeterProvider.builder()
 *     .withPeriodicExporter(exporter)
 *     .build()
 * Global.setMeterProvider(meterProvider)
 *
 * // Get a meter
 * val meter = Global.meter("my_meter")
 *
 * // Create a metric
 * val counter = meter.uint64Counter("my_counter").build()
 * counter.add(1, listOf(KeyValue("key", "value")))
 *
 * // Shutdown the meter provider. This will trigger an export of all metrics.
 * meterProvider.shutdown()
 * ```
 *
 * After running your application configured with the OTLP exporter, view metrics at:
 * `http://localhost:9090`
 * ## Show Logs, Metrics too (TODO)
 *
 * # Feature Flags
 * The following feature flags can enable exporters for different telemetry signals:
 *
 * * `trace`: Includes the trace exporters.
 * * `metrics`: Includes the metrics exporters.
 * * `logs`: Includes the logs exporters.
 *
 * The following feature flags generate additional code and types:
 * * `serialize`: Enables serialization support for types defined in this crate.
 *
 * The following feature flags offer additional configurations on gRPC:
 *
 * For users using `tonic` as grpc layer:
 * * `grpc-tonic`: Use `tonic` as grpc layer.
 * * `gzip-tonic`: Use gzip compression for `tonic` grpc layer.
 * * `zstd-tonic`: Use zstd compression for `tonic` grpc layer.
 * * `tls-roots`: Adds system trust roots to rustls-based gRPC clients using the rustls-native-certs crate.
 * * `tls-webpki-roots`: Embeds Mozilla's trust roots to rustls-based gRPC clients using the webpki-roots crate.
 *
 * The following feature flags offer additional configurations on http:
 *
 * * `http-proto`: Use http as transport layer, protobuf as body format. This feature is enabled by default.
 * * `gzip-http`: Use gzip compression for HTTP transport.
 * * `zstd-http`: Use zstd compression for HTTP transport.
 * * `reqwest-blocking-client`: Use reqwest blocking http client. This feature is enabled by default.
 * * `reqwest-client`: Use reqwest http client.
 * * `reqwest-rustls`: Use reqwest with TLS with system trust roots via `rustls-native-certs` crate.
 * * `reqwest-rustls-webpki-roots`: Use reqwest with TLS with Mozilla's trust roots via `webpki-roots` crate.
 *
 * # Kitchen Sink Full Configuration
 *
 * Example showing how to override all configuration options.
 *
 * Generally there are two parts of configuration. One is the exporter, the other is the provider.
 * Users can configure the exporter using [SpanExporter.builder] for traces, and
 * [MetricExporter.builder] + `PeriodicReader.builder()` for metrics. Once you have an exporter,
 * you can add it to either an `SdkTracerProvider.builder()` for traces, or
 * an `SdkMeterProvider.builder()` for metrics.
 */

/** The communication protocol to use when exporting data. */
public enum class Protocol {
    /** GRPC protocol */
    Grpc,

    /** HTTP protocol with binary protobuf */
    HttpBinary,

    /** HTTP protocol with JSON payload */
    HttpJson,
}

/** Type to indicate the builder does not have a client set. */
public class NoExporterBuilderSet {
    override fun toString(): String = "NoExporterBuilderSet"
}

/** Placeholder type when no exporter pipeline has been configured in telemetry pipeline. */
public class NoExporterConfig {
    override fun toString(): String = "NoExporterConfig"
}
