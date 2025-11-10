package com.example.stundenplan.web.dto;

public record PlanItemDto(
        String wochentag,   // MONTAG, DIENSTAG...
        int startStunde,    // 1..N
        int endStunde,      // 1..N
        String fach,        // "Mathematik"
        String lehrer,      // "Vorname Nachname"
        String raum         // "R101" Falls es existiert
) {}
