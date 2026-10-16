# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 2/13 (15.4%)
- **Function parity:** 5/183 matched (target 34) — 2.7%
- **Class/type parity:** 8/39 matched (target 15) — 20.5%
- **Combined symbol parity:** 13/222 matched (target 49) — 5.9%
- **Average inline-code cosine:** 0.00 (function body across 0 matched files)
- **Average documentation cosine:** 0.00 (doc text across 0 matched files)
- **Cheat-zeroed Files:** 2
- **Critical Issues:** 2 files with <0.60 function similarity
- **Needs Review:** 0 files with 0.60-0.84 function similarity
- **Excellent:** 0 files with >=0.85 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. exporter.mod

- **Target:** `exporter.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 243410.0
- **Functions:** 5/28 matched (target 32)
- **Missing functions:** `default`, `fmt`, `from_str`, `resolve_compression_from_env`, `export_config`, `export_config`, `with_endpoint`, `with_protocol`, `with_timeout`, `with_export_config`, `resolve_timeout`, `run_env_test`, `test_default_http_endpoint`, `export_builder_error_invalid_http_endpoint`, `export_builder_error_invalid_grpc_endpoint`, `test_default_tonic_endpoint`, `test_default_protocol`, `test_url_decode`, `test_parse_header_string`, `test_parse_header_key_value_string`, `test_priority_of_signal_env_over_generic_env_for_timeout`, `test_priority_of_code_based_config_over_envs_for_timeout`, `test_use_default_when_others_missing_for_timeout`
- **Types:** 5/6 matched (target 12)
- **Missing types:** `Err`
- **Tests:** 0/12 matched

### 2. lib

- **Target:** `opentelemetryotlp.Lib [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 20510.0
- **Functions:** 0/0 matched (target 2)
- **Missing functions:** _none_
- **Types:** 3/5 matched (target 3)
- **Missing types:** `TonicExporterBuilderSet`, `HttpExporterBuilderSet`

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

