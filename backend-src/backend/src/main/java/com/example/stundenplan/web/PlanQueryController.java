package com.example.stundenplan.web;

import com.example.stundenplan.service.PlanQueryService;
import com.example.stundenplan.web.dto.GridPlanDto;
import com.example.stundenplan.web.dto.KlassenplanDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PlanQueryController {
  private final PlanQueryService service;
  public PlanQueryController(PlanQueryService service){ this.service = service; }

  @GetMapping("/klassen/{id}/stundenplan")
  public KlassenplanDto getKlassenplan(@PathVariable Long id){
    return service.getKlassenplan(id);
  }

  @GetMapping("/lehrer/{id}/stundenplan")
  public KlassenplanDto getLehrerplan(@PathVariable Long id){
    return service.getLehrerplan(id);
  }

  @GetMapping("/klassen/{id}/stundenplan/grid")
  public GridPlanDto getKlassenGrid(@PathVariable Long id){
    return service.getKlassenplanGrid(id);
  }

  @GetMapping("/lehrer/{id}/stundenplan/grid")
  public GridPlanDto getLehrerGrid(@PathVariable Long id){
    return service.getLehrerplanGrid(id);
  }

  @GetMapping("/raeume/{id}/belegung")
  public GridPlanDto getRaum(@PathVariable Long id){
    return service.getRaumbelegung(id);
  }




}

