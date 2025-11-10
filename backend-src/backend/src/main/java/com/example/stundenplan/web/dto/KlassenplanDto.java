package com.example.stundenplan.web.dto;

import java.util.List;

public record KlassenplanDto(
        Long klasseId,
        String klasse,
        List<PlanItemDto> items
) {}
