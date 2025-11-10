package com.example.stundenplan.web.dto;

public record GridCellDto(
  int startStunde,
  int endStunde,
  String fach,
  String sub,      // "Lehrer" oder "Klasse"
  String raum
) {}