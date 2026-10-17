package io.github.kotlinmania.opentelemetryotlp

import io.github.kotlinmania.opentelemetryotlp.exporter.ExportConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.HasExportConfig
import io.github.kotlinmania.opentelemetryotlp.exporter.http.HttpExporterBuilder
import io.github.kotlinmania.opentelemetryotlp.exporter.tonic.TonicExporterBuilder

/** Type to indicate the builder does not have a client set. */
public class NoExporterBuilderSet {
    override fun toString(): String = "NoExporterBuilderSet"
}

/**
 * Type to hold the [TonicExporterBuilder] and indicate it has been set.
 *
 * Allowing access to [TonicExporterBuilder] specific configuration methods.
 */
public class TonicExporterBuilderSet(
    public val builder: TonicExporterBuilder = TonicExporterBuilder(),
) : HasExportConfig {
    override var exportConfig: ExportConfig
        get() = builder.exportConfig
        set(value) {
            builder.exportConfig = value
        }

    override fun toString(): String = "TonicExporterBuilderSet"
}

/**
 * Type to hold the [HttpExporterBuilder] and indicate it has been set.
 *
 * Allowing access to [HttpExporterBuilder] specific configuration methods.
 */
public class HttpExporterBuilderSet(
    public val builder: HttpExporterBuilder = HttpExporterBuilder(),
) : HasExportConfig {
    override var exportConfig: ExportConfig
        get() = builder.exportConfig
        set(value) {
            builder.exportConfig = value
        }

    override fun toString(): String = "HttpExporterBuilderSet"
}
