package com.example.stundenplan.web.dto;

import jakarta.validation.constraints.NotNull;

public record CreateUnterrichtRequest(
  @NotNull Long klasseId,
  @NotNull Long fachId,
  @NotNull Long lehrerId
) {}
