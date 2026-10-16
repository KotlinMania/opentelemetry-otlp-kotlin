// port-lint: source lib.rs
package io.github.kotlinmania.opentelemetryotlp

import kotlinx.serialization.Serializable

/**
 * The communication protocol to use when exporting data.
 */
@Serializable
public enum class Protocol {
    /** GRPC protocol */
    Grpc,

    /** HTTP protocol with binary protobuf */
    HttpBinary,

    /** HTTP protocol with JSON payload */
    HttpJson,
}
