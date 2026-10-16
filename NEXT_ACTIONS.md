# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 13/13 (100.0%)
- **Function parity:** 84/183 matched (target 112) — 45.9%
- **Class/type parity:** 32/39 matched (target 46) — 82.1%
- **Combined symbol parity:** 116/222 matched (target 158) — 52.3%
- **Average inline-code cosine:** 0.06 (function body across 10 matched files)
- **Average documentation cosine:** 0.24 (doc text across 10 matched files)
- **Cheat-zeroed Files:** 4
- **Critical Issues:** 13 files with <0.60 function similarity
- **Needs Review:** 0 files with 0.60-0.84 function similarity
- **Excellent:** 0 files with >=0.85 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. http.mod

- **Target:** `http.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 506010.0
- **Functions:** 5/54 matched (target 11)
- **Missing functions:** `default`, `build_client`, `resolve_compression`, `process_body`, `new`, `build_trace_export_body`, `build_logs_export_body`, `build_metrics_export_body`, `build_endpoint_uri`, `resolve_http_endpoint`, `add_header_from_string`, `http_client_config`, `with_http_client`, `test_append_signal_path_to_generic_env`, `test_not_append_signal_path_to_signal_env`, `test_priority_of_signal_env_over_generic_env`, `test_priority_of_code_based_config_over_envs`, `test_use_default_when_empty_string_for_option`, `test_use_default_when_others_missing`, `test_build_endpoint_uri`, `test_invalid_uri_in_signal_env_falls_back_to_generic_env`, `test_all_invalid_urls_falls_back_to_error`, `test_add_header_from_string`, `test_merge_header_from_string`, `test_http_exporter_builder_with_headers`, `test_http_exporter_endpoint`, `test_gzip_compression_and_decompression`, `test_zstd_compression_and_decompression`, `test_no_compression_when_disabled`, `test_gzip_error_when_feature_disabled`, `test_zstd_error_when_feature_disabled`, `send_bytes`, `send_bytes`, `create_test_client`, `create_test_span_data`, `test_build_trace_export_body_binary_protocol`, `test_build_trace_export_body_json_protocol`, `test_build_trace_export_body_with_compression`, `create_test_log_batch`, `test_build_logs_export_body_binary_protocol`, `test_build_logs_export_body_json_protocol`, `test_build_logs_export_body_with_compression`, `test_build_metrics_export_body_binary_protocol`, `test_build_metrics_export_body_json_protocol`, `test_build_metrics_export_body_with_compression`, `test_build_metrics_export_body_compression_error_returns_none`, `test_resolve_compression_uses_generic_env_fallback`, `test_build_span_exporter_with_gzip_without_feature`, `test_build_span_exporter_with_zstd_without_feature`
- **Types:** 5/6 matched (target 5)
- **Missing types:** `MockHttpClient`
- **Tests:** 0/36 matched

### 2. tonic.mod

- **Target:** `tonic.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 283710.0
- **Functions:** 5/31 matched (target 9)
- **Missing functions:** `try_from`, `call`, `fmt`, `default`, `build_channel`, `resolve_endpoint`, `resolve_compression`, `merge_metadata_with_headers_from_env`, `parse_headers_from_env`, `tonic_config`, `with_tls_config`, `with_channel`, `with_interceptor`, `test_with_metadata`, `test_with_gzip_compression`, `test_with_zstd_compression`, `test_convert_compression`, `test_priority_of_signal_env_over_generic_env_for_compression`, `test_priority_of_code_based_config_over_envs_for_compression`, `test_use_default_when_others_missing_for_compression`, `test_parse_headers_from_env`, `test_merge_metadata_with_headers_from_env`, `test_priority_of_signal_env_over_generic_env_for_endpoint`, `test_priority_of_code_based_config_over_envs_for_endpoint`, `test_use_default_when_others_missing_for_endpoint`, `test_use_default_when_empty_string_for_option`
- **Types:** 4/6 matched (target 4)
- **Missing types:** `Error`, `BoxInterceptor`
- **Tests:** 0/13 matched

### 3. exporter.mod

- **Target:** `exporter.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 183410.0
- **Functions:** 11/28 matched (target 23)
- **Missing functions:** `default`, `fmt`, `from_str`, `resolve_compression_from_env`, `export_config`, `export_config`, `with_endpoint`, `with_protocol`, `with_timeout`, `with_export_config`, `resolve_timeout`, `run_env_test`, `export_builder_error_invalid_http_endpoint`, `export_builder_error_invalid_grpc_endpoint`, `test_priority_of_signal_env_over_generic_env_for_timeout`, `test_priority_of_code_based_config_over_envs_for_timeout`, `test_use_default_when_others_missing_for_timeout`
- **Types:** 5/6 matched (target 12)
- **Missing types:** `Err`
- **Tests:** 6/12 matched

### 4. tonic.trace

- **Target:** `tonic.Trace`
- **Similarity:** 0.02
- **Dependents:** 0
- **Priority Score:** 20709.8
- **Functions:** 4/5 matched (target 4)
- **Missing functions:** `fmt`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `ClientInner`
- **Lint issues:** 1

### 5. tonic.logs

- **Target:** `tonic.Logs`
- **Similarity:** 0.03
- **Dependents:** 0
- **Priority Score:** 20709.7
- **Functions:** 4/5 matched (target 4)
- **Missing functions:** `fmt`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `ClientInner`
- **Lint issues:** 1

### 6. tonic.metrics

- **Target:** `tonic.Metrics`
- **Similarity:** 0.01
- **Dependents:** 0
- **Priority Score:** 20609.9
- **Functions:** 3/4 matched (target 3)
- **Missing functions:** `fmt`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `ClientInner`

### 7. http.trace

- **Target:** `http.Trace`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 20310.0
- **Functions:** 1/3 matched (target 1)
- **Missing functions:** `shutdown`, `set_resource`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 8. http.logs

- **Target:** `http.Logs`
- **Similarity:** 0.01
- **Dependents:** 0
- **Priority Score:** 10309.9
- **Functions:** 2/3 matched (target 2)
- **Missing functions:** `set_resource`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 9. http.metrics

- **Target:** `http.Metrics`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10210.0
- **Functions:** 1/2 matched (target 1)
- **Missing functions:** `shutdown`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 10. metric

- **Target:** `opentelemetryotlp.Metric`
- **Similarity:** 0.16
- **Dependents:** 0
- **Priority Score:** 2308.4
- **Functions:** 19/19 matched (target 20)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 7)
- **Missing types:** _none_

### 11. logs

- **Target:** `opentelemetryotlp.Logs`
- **Similarity:** 0.20
- **Dependents:** 0
- **Priority Score:** 1808.0
- **Functions:** 15/15 matched
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_

### 12. span

- **Target:** `opentelemetryotlp.Span`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 1708.1
- **Functions:** 14/14 matched (target 15)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_

### 13. lib

- **Target:** `opentelemetryotlp.Mod [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 510.0
- **Functions:** 0/0 matched (target 4)
- **Missing functions:** _none_
- **Types:** 5/5 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

