package org.ebndrnk.leverxfinalproject.util.feature;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeatureFlags {
    CALL_PERFORMANCE_ASPECT_FEATURE_FLAG("performance_aspect_enabled");

    private final String value;
}
