package com.thesdestudio.personal;

import jakarta.validation.constraints.NotNull;

public record UpdateRevisitLaterRequest(
        @NotNull(message = "Revisit-later status is required")
        Boolean revisitLater
) {
}
