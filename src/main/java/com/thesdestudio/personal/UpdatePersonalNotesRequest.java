package com.thesdestudio.personal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePersonalNotesRequest(
        @NotNull(message = "Notes cannot be null")
        @Size(max = 10000, message = "Notes cannot exceed 10000 characters")
        String notes
) {
}
