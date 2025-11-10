package com.example.stundenplan.web.dto;

import jakarta.validation.constraints.NotNull;

public record CreateUnterrichtsstundeRequest(
  @NotNull Long unterrichtId,
  @NotNull Long zeitSlotId,
  Long raumAbweichungId
) {}
