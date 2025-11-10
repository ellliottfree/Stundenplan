package com.example.stundenplan.web.dto;

import java.util.List;
import java.util.Map;

public record GridPlanDto(
  Long id,         // Klassen oder Lehrer Id
  String title,    // "10A" oder "Gökhan Bayrakci"
  Map<String, List<GridCellDto>> days // "MONTAG" -> [cells...]
) {}