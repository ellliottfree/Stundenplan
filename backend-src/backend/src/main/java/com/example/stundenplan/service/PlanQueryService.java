package com.example.stundenplan.service;

import com.example.stundenplan.domain.*;
import com.example.stundenplan.repo.LehrerRepo;
import com.example.stundenplan.repo.SchulklasseRepo;
import com.example.stundenplan.repo.UnterrichtsstundeRepo;
import com.example.stundenplan.web.dto.GridPlanDto;
import com.example.stundenplan.web.dto.KlassenplanDto;
import com.example.stundenplan.web.dto.PlanItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanQueryService {

    private final SchulklasseRepo schulklasseRepo;
    private final LehrerRepo lehrerRepo;
    private final UnterrichtsstundeRepo unterrichtsstundeRepo;

    
    private PlanItemDto mapToPlanItem(Unterrichtsstunde us) {
        ZeitSlot zs = us.getZeitSlot();
        Unterricht u = us.getUnterricht();
        Fach f = u.getFach();
        Lehrer l = u.getLehrer();

        String lehrerName = (l != null) ? l.getVorname() + " " + l.getNachname() : "N/A";
        String fachName = (f != null) ? f.getBezeichnung() : "N/A";

        // Finde den Raum
        Raum r = us.getRaumAbweichung();
        String raumName = null;
        if (r != null) {
            // Fall 1: Abweichender Raum (z.B. Turnhalle)
            raumName = r.getBezeichnung();
        } else {
            // Fall 2: Standard-Klassenzimmer
            raumName = u.getSchulklasse().getKlassenzimmer().getBezeichnung();
        }

        
        
        return new PlanItemDto(
                us.getId(), // <-- Argument 1 (Long)
                zs.getWochentag().name(),
                zs.getStartStunde(),
                zs.getEndStunde(),
                fachName,
                lehrerName,
                raumName
        );
    }
    

    public KlassenplanDto getKlassenplan(Long klasseId) {
        Schulklasse klasse = schulklasseRepo.findById(klasseId)
                .orElseThrow(() -> new RuntimeException("Klasse nicht gefunden"));

        List<Unterrichtsstunde> stunden = unterrichtsstundeRepo.findAllByKlasse(klasseId);

        List<PlanItemDto> items = stunden.stream()
                .map(this::mapToPlanItem)
                .sorted(Comparator.comparing(PlanItemDto::wochentag)
                        .thenComparing(PlanItemDto::startStunde))
                .toList();

        return new KlassenplanDto(klasse.getId(), klasse.getBezeichnung(), items);
    }

    
    public KlassenplanDto getLehrerplan(Long lehrerId) {
        Lehrer lehrer = lehrerRepo.findById(lehrerId)
                .orElseThrow(() -> new RuntimeException("Lehrer nicht gefunden"));

        
        List<Unterrichtsstunde> stunden = unterrichtsstundeRepo.findAllByLehrerOrdered(lehrerId);

        List<PlanItemDto> items = stunden.stream()
                .map(this::mapToPlanItem)
                .sorted(Comparator.comparing(PlanItemDto::wochentag)
                        .thenComparing(PlanItemDto::startStunde))
                .toList();

        return new KlassenplanDto(lehrer.getId(), lehrer.getNachname(), items);
    }
    

    // (Der Rest der Methoden bleibt unverändert)
    public GridPlanDto getKlassenplanGrid(Long id) {
        return null; // Nicht implementiert
    }
    public GridPlanDto getLehrerplanGrid(Long id) {
        return null; // Nicht implementiert
    }
    public GridPlanDto getRaumbelegung(Long id) {
        return null; // Nicht implementiert
    }
}