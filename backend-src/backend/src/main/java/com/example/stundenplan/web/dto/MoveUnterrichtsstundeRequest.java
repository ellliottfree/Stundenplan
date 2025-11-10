package com.example.stundenplan.web.dto;

import jakarta.validation.constraints.NotNull;

public record MoveUnterrichtsstundeRequest(
  @NotNull Long zeitSlotId,
  Long raumAbweichungId
) {}
