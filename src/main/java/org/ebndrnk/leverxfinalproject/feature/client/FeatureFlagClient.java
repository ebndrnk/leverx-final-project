package org.ebndrnk.leverxfinalproject.feature.client;

import org.ebndrnk.leverxfinalproject.model.dto.feature.FeatureFlagResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Feign client for interacting with SAP Feature Flags Service.
 * Used to retrieve the status of feature flags by their name.
 */
@Component
@FeignClient(name = "featureFlagClient", url = "${sap.feature.flags.url}")
public interface FeatureFlagClient {

    /**
     * Retrieves the status of a given feature flag.
     *
     * @param featureName the name of the feature flag to check.
     * @param authHeader  the authorization header in the format "Basic base64(username:password)".
     * @return {@link FeatureFlagResponse} containing the feature flag status.
     */
    @GetMapping("/api/v2/evaluate/{featureName}")
    FeatureFlagResponse getFeatureFlag(
            @PathVariable("featureName") String featureName,
            @RequestHeader("Authorization") String authHeader
    );
}
