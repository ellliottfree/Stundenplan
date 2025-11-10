package com.example.stundenplan.web;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.stundenplan.repo.*;
import com.example.stundenplan.domain.*;

@RestController @RequestMapping("/api/v1/lehrer")
class LehrerController {
  private final LehrerRepo repo; LehrerController(LehrerRepo r){repo=r;}
  @GetMapping List<Lehrer> all(){ return repo.findAll(); }
}

@RestController @RequestMapping("/api/v1/faecher")
class FachController {
  private final FachRepo repo; FachController(FachRepo r){repo=r;}
  @GetMapping List<Fach> all(){ return repo.findAll(); }
}

@RestController @RequestMapping("/api/v1/klassen")
class SchulklasseController {
  private final SchulklasseRepo repo; SchulklasseController(SchulklasseRepo r){repo=r;}
  @GetMapping List<Schulklasse> all(){ return repo.findAll(); }
}

@RestController @RequestMapping("/api/v1/schueler")
class SchuelerController {
  private final SchuelerRepo repo; SchuelerController(SchuelerRepo r){repo=r;}
  @GetMapping List<Schueler> all(@RequestParam(required=false) Long klasseId){
    return klasseId==null ? repo.findAll() : repo.findBySchulklasseId(klasseId);
  }
}

@RestController @RequestMapping("/api/v1/unterricht")
class UnterrichtController {
  private final UnterrichtRepo repo; UnterrichtController(UnterrichtRepo r){repo=r;}
  @GetMapping List<Unterricht> byKlasse(@RequestParam Long klasseId){
    return repo.findBySchulklasseId(klasseId);
  }
}

@RestController @RequestMapping("/api/v1/unterrichtsstunden")
class UnterrichtsstundeController {
  private final UnterrichtsstundeRepo repo; UnterrichtsstundeController(UnterrichtsstundeRepo r){repo=r;}
  @GetMapping List<Unterrichtsstunde> byKlasse(@RequestParam Long klasseId){
    return repo.findAllByKlasse(klasseId);
  }
}

