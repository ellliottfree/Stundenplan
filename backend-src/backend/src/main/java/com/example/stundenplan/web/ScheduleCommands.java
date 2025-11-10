package com.example.stundenplan.web;

import org.springframework.web.bind.annotation.*;
import com.example.stundenplan.service.ScheduleService;
import com.example.stundenplan.domain.*;
import com.example.stundenplan.web.dto.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ScheduleCommands {

  private final ScheduleService service;
  public ScheduleCommands(ScheduleService service){ this.service = service; }

  @PostMapping("/unterricht")
  public Unterricht createUnterricht(@RequestBody @Valid CreateUnterrichtRequest req) {
    return service.addUnterricht(req.klasseId(), req.fachId(), req.lehrerId());
  }

  @PostMapping("/unterrichtsstunden")
  public Unterrichtsstunde createUnterrichtsstunde(@RequestBody @Valid CreateUnterrichtsstundeRequest req) {
    return service.addUnterrichtsstunde(req.unterrichtId(), req.zeitSlotId(), req.raumAbweichungId());
  }

  @DeleteMapping("/unterricht/{id}")
  public void deleteUnterricht(@PathVariable Long id) {
    service.deleteUnterricht(id);
  }

  @DeleteMapping("/unterrichtsstunden/{id}")
  public void deleteUnterrichtsstunde(@PathVariable Long id) {
    service.deleteUnterrichtsstunde(id);
  }

  @PatchMapping("/unterrichtsstunden/{id}")
  public Unterrichtsstunde moveUnterrichtsstunde(@PathVariable Long id,
      @RequestBody @jakarta.validation.Valid MoveUnterrichtsstundeRequest req) {
    return service.moveUnterrichtsstunde(id, req.zeitSlotId(), req.raumAbweichungId());
  }

}

