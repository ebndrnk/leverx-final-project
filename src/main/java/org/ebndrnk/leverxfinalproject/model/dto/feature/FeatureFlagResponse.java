package org.ebndrnk.leverxfinalproject.model.dto.feature;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FeatureFlagResponse {
    @JsonProperty("featureName")
    private String featureName;

    @JsonProperty("type")
    private String type;

    @JsonProperty("variation")
    private boolean variation;
}
