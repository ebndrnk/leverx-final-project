package org.ebndrnk.leverxfinalproject.util.feature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.ebndrnk.leverxfinalproject.model.dto.feature.FeatureFlagResponse;
import org.ebndrnk.leverxfinalproject.util.feature.client.FeatureFlagClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Service for interacting with SAP Feature Flags Service.
 * It retrieves the status of feature flags and manages authentication.
 */
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class FeatureFlagServiceImpl implements FeatureFlagService{

    private final FeatureFlagClient featureFlagClient;

    @Value("${sap.feature.flags.username}")
    private String username;

    @Value("${sap.feature.flags.password}")
    private String password;

    /**
     * Generates a Basic Authentication header.
     *
     * @return A Base64-encoded authentication header.
     */
    private String getBasicAuthHeader() {
        String auth = username + ":" + password;
        String encodedAuth = Base64.encodeBase64String(auth.getBytes());
        return "Basic " + encodedAuth;
    }

    /**
     * Checks if a given feature flag is enabled.
     *
     * @param featureName The name of the feature flag to check.
     * @return {@code true} if the feature flag is enabled, otherwise {@code false}.
     */
    @Override
    public boolean isFeatureEnabled(String featureName) {
        try {
            FeatureFlagResponse response = featureFlagClient.getFeatureFlag(featureName, getBasicAuthHeader());
            return response != null && response.isVariation();
        } catch (Exception e) {
            log.error("Error retrieving feature flag '{}': {}", featureName, e.getMessage());
            return false;
        }
    }
}
