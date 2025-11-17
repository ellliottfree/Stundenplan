// H:\...Stundenplan\backend-src\backend\src\main\java\...\web\BasicControllers.java

package com.example.stundenplan.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.stundenplan.repo.*;
import com.example.stundenplan.domain.*;

// Korrigierte Importe
import com.example.stundenplan.web.dto.KlassenplanDto; 
import com.example.stundenplan.web.dto.PlanItemDto;
import com.example.stundenplan.service.PlanQueryService;

// --- Lehrer Controller  ---
@RestController @RequestMapping("/api/v1/lehrer")
class LehrerController {
    private final LehrerRepo repo; 
    LehrerController(LehrerRepo r){repo=r;}
    @GetMapping 
    List<Lehrer> all(){ return repo.findAll(); }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Lehrer createLehrer(@RequestBody Lehrer lehrer) {
        return repo.save(lehrer);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLehrer(@PathVariable Long id) {
        repo.deleteById(id);
    }
}

// --- Fach Controller  ---
@RestController @RequestMapping("/api/v1/faecher")
class FachController {
    private final FachRepo repo; 
    FachController(FachRepo r){repo=r;}
    @GetMapping 
    List<Fach> all(){ return repo.findAll(); }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fach createFach(@RequestBody Fach fach) {
        return repo.save(fach);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFach(@PathVariable Long id) {
        repo.deleteById(id);
    }
}

// --- Schulklasse Controller  ---
@RestController @RequestMapping("/api/v1/klassen")
class SchulklasseController {
    private final SchulklasseRepo repo; 
    SchulklasseController(SchulklasseRepo r){repo=r;}
    @GetMapping 
    List<Schulklasse> all(){ return repo.findAll(); }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Schulklasse createKlasse(@RequestBody Schulklasse klasse) {
        return repo.save(klasse);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteKlasse(@PathVariable Long id) {
        repo.deleteById(id);
    }
}



// --- Schueler Controller (Schüler) ---
@RestController @RequestMapping("/api/v1/schueler")
class SchuelerController {
    private final SchuelerRepo repo; 
    
    // Wir brauchen auch das Klassen-Repo, um Schüler zuzuweisen
    private final SchulklasseRepo schulklasseRepo;

    // Konstruktor erweitert
    SchuelerController(SchuelerRepo r, SchulklasseRepo kr){
        this.repo = r;
        this.schulklasseRepo = kr;
    }
    
    // GET (War schon da)
    @GetMapping List<Schueler> all(@RequestParam(required=false) Long klasseId){
        return klasseId==null ? repo.findAll() : repo.findBySchulklasseId(klasseId);
    }


    // Wir nehmen ein DTO an, das die klasseId enthält
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Schueler createSchueler(@RequestBody SchuelerCreateRequest req) {
        // Finde die Klasse, zu der der Schüler gehört
        Schulklasse klasse = schulklasseRepo.findById(req.schulklasseId())
            .orElseThrow(() -> new RuntimeException("Klasse nicht gefunden"));
        
        Schueler schueler = new Schueler();
        schueler.setVorname(req.vorname());
        schueler.setNachname(req.nachname());
        schueler.setSchulklasse(klasse); // Verknüpfung
        
        return repo.save(schueler);
    }

    // --- DELETE (Löschen) ---
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSchueler(@PathVariable Long id) {
        repo.deleteById(id);
    }
    
    // Ein DTO (Record) für die Erstellungsanfrage

    public record SchuelerCreateRequest(String vorname, String nachname, Long schulklasseId) {}
}



// --- Unterricht Controller ---
@RestController @RequestMapping("/api/v1/unterricht")
class UnterrichtController {
    private final UnterrichtRepo repo; 
    UnterrichtController(UnterrichtRepo r){repo=r;}
    @GetMapping List<Unterricht> byKlasse(@RequestParam Long klasseId){
        return repo.findBySchulklasseId(klasseId);
    }
}

// --- Unterrichtsstunde Controller  ---
@RestController @RequestMapping("/api/v1/unterrichtsstunden")
class UnterrichtsstundeController {
    private final PlanQueryService planQueryService;
    public UnterrichtsstundeController(PlanQueryService pqs){
        this.planQueryService = pqs;
    }
    @GetMapping
    public List<PlanItemDto> byKlasse(@RequestParam Long klasseId){
        KlassenplanDto plan = planQueryService.getKlassenplan(klasseId); 
        if (plan != null && plan.items() != null) { 
            return plan.items();
        }
        return Collections.emptyList();
    }
}