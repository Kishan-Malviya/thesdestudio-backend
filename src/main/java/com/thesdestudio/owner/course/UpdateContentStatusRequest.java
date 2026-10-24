package com.thesdestudio.owner.course;

import com.thesdestudio.common.enums.ContentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateContentStatusRequest(
        @NotNull(message = "Status is required")
        ContentStatus status
) {
}
