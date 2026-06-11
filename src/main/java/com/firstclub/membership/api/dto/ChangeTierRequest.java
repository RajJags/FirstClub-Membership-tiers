package com.firstclub.membership.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeTierRequest(@NotBlank String tierCode) {
}
