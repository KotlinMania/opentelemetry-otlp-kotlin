// port-lint: source exporter/mod.rs
package io.github.kotlinmania.opentelemetryotlp.exporter

import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * Errors that can occur while building an exporter.
 *
 * Marked non-exhaustive in upstream to allow for future expansion without
 * breaking changes. This could be refined after polishing and finalizing the
 * errors.
 */
@OptIn(ExperimentalObjCRefinement::class)
@HiddenFromObjC
public sealed class ExporterBuildError(
    message: String,
) : Exception(message) {
    /** Spawning a new thread failed. */
    public object ThreadSpawnFailed :
        ExporterBuildError("Spawning a new thread failed. Unable to create Reqwest-Blocking client.")

    /** Feature required to use the specified compression algorithm. */
    public class FeatureRequiredForCompressionAlgorithm(
        public val feature: String,
        public val compression: Compression,
    ) : ExporterBuildError("feature '$feature' is required to use the compression algorithm '$compression'")

    /** No Http client specified. */
    public object NoHttpClient : ExporterBuildError("no http client specified")

    /** Unsupported compression algorithm. */
    public class UnsupportedCompressionAlgorithm(
        public val algorithm: String,
    ) : ExporterBuildError("unsupported compression algorithm '$algorithm'")

    /** Invalid URI. */
    public class InvalidUri(
        public val uri: String,
        public val reason: String,
    ) : ExporterBuildError("invalid URI $uri. Reason $reason")

    /**
     * Failed due to an internal error.
     * The error message is intended for logging purposes only and should not
     * be used to make programmatic decisions. It is implementation-specific
     * and subject to change without notice. Consumers of this error should not
     * rely on its content beyond logging.
     */
    public class InternalFailure(
        public val reason: String,
    ) : ExporterBuildError("Reason: $reason")
}
