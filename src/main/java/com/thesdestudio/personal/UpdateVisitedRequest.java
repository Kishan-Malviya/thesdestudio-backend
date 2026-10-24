package com.thesdestudio.personal;

import jakarta.validation.constraints.NotNull;

public record UpdateVisitedRequest(
        @NotNull(message = "Visited status is required")
        Boolean visited
) {
}
