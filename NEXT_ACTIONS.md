# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 9/13 (69.2%)
- **Function parity:** 70/183 matched (target 92) — 38.3%
- **Class/type parity:** 16/39 matched (target 23) — 41.0%
- **Combined symbol parity:** 86/222 matched (target 115) — 38.7%
- **Average inline-code cosine:** 0.10 (function body across 9 matched files)
- **Average documentation cosine:** 0.26 (doc text across 9 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 9 files with <0.60 function similarity
- **Needs Review:** 0 files with 0.60-0.84 function similarity
- **Excellent:** 0 files with >=0.85 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. metric

- **Target:** `opentelemetryotlp.Metric`
- **Similarity:** 0.17
- **Dependents:** 0
- **Priority Score:** 2308.3
- **Functions:** 19/19 matched (target 22)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 7)
- **Missing types:** _none_

### 2. logs

- **Target:** `opentelemetryotlp.Logs`
- **Similarity:** 0.20
- **Dependents:** 0
- **Priority Score:** 1808.0
- **Functions:** 15/15 matched (target 17)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_

### 3. span

- **Target:** `opentelemetryotlp.Span`
- **Similarity:** 0.20
- **Dependents:** 0
- **Priority Score:** 1708.0
- **Functions:** 14/14 matched (target 21)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_

### 4. tonic.trace

- **Target:** `tonic.Trace`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 709.3
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

### 5. tonic.logs

- **Target:** `tonic.Logs`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 709.3
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

### 6. tonic.metrics

- **Target:** `tonic.Metrics`
- **Similarity:** 0.02
- **Dependents:** 0
- **Priority Score:** 609.8
- **Functions:** 4/4 matched (target 5)
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

### 7. http.trace

- **Target:** `http.Trace`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 309.3
- **Functions:** 3/3 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 8. http.logs

- **Target:** `http.Logs`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 309.3
- **Functions:** 3/3 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 9. http.metrics

- **Target:** `http.Metrics`
- **Similarity:** 0.02
- **Dependents:** 0
- **Priority Score:** 209.8
- **Functions:** 2/2 matched (target 3)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

