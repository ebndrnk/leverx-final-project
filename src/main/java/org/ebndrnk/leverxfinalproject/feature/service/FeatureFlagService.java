package org.ebndrnk.leverxfinalproject.feature.service;

public interface FeatureFlagService {
    boolean isFeatureEnabled(String featureName);
}
