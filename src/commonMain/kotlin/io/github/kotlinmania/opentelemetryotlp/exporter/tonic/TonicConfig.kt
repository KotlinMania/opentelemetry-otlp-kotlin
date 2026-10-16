// port-lint: source exporter/tonic/mod.rs
package io.github.kotlinmania.opentelemetryotlp.exporter.tonic

import io.github.kotlinmania.opentelemetryotlp.exporter.Compression

/**
 * Configuration for tonic grpc transport.
 */
public class TonicConfig(
    public var metadata: Map<String, String>? = null,
    public var compression: Compression? = null,
)

/** Provide access to the [TonicConfig] field within the exporter builders. */
public interface HasTonicConfig {
    public var tonicConfig: TonicConfig
}

/** Expose methods to override [TonicConfig]. */
public interface WithTonicConfig<T : WithTonicConfig<T>> {
    public fun withMetadata(metadata: Map<String, String>): T
    public fun withCompression(compression: Compression): T
}
