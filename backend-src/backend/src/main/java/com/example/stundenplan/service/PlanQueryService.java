package com.example.stundenplan.service;

import com.example.stundenplan.domain.*;
import com.example.stundenplan.repo.*;
import com.example.stundenplan.web.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlanQueryService {
  private final SchulklasseRepo klasseRepo;
  private final UnterrichtsstundeRepo ustdRepo;

  public PlanQueryService(SchulklasseRepo klasseRepo, UnterrichtsstundeRepo ustdRepo) {
    this.klasseRepo = klasseRepo;
    this.ustdRepo = ustdRepo;
  }

  @Transactional(readOnly = true)
  public KlassenplanDto getKlassenplan(Long klasseId) {
    Schulklasse klasse = klasseRepo.findById(klasseId)
            .orElseThrow(() -> new IllegalArgumentException("Klasse nicht gefunden"));

    List<Unterrichtsstunde> stunden = ustdRepo.findAllByKlasse(klasseId);

    List<PlanItemDto> items = stunden.stream().map(us -> {
      ZeitSlot zs = us.getZeitSlot();
      Unterricht u = us.getUnterricht();
      String fach = u.getFach().getBezeichnung();
      String lehrer = u.getLehrer().getVorname() + " " + u.getLehrer().getNachname();

      String raumBez = (us.getRaumAbweichung() != null)
              ? us.getRaumAbweichung().getBezeichnung()
              : u.getSchulklasse().getKlassenzimmer().getBezeichnung();

      return new PlanItemDto(
              zs.getWochentag().name(),
              zs.getStartStunde(),
              zs.getEndStunde(),
              fach,
              lehrer,
              raumBez
      );
    }).toList();

    return new KlassenplanDto(klasse.getId(), klasse.getBezeichnung(), items);
  }

  @Transactional(readOnly = true)
public KlassenplanDto getLehrerplan(Long lehrerId) {
  List<Unterrichtsstunde> stunden = ustdRepo.findAllByLehrerOrdered(lehrerId);

  String header = stunden.isEmpty()
      ? ("Lehrer #" + lehrerId)
      : (stunden.get(0).getUnterricht().getLehrer().getVorname() + " " +
         stunden.get(0).getUnterricht().getLehrer().getNachname());

  List<PlanItemDto> items = stunden.stream().map(us -> {
    ZeitSlot zs = us.getZeitSlot();
    Unterricht u = us.getUnterricht();
    String fach = u.getFach().getBezeichnung();
    String klasse = u.getSchulklasse().getBezeichnung();
    String raumBez = (us.getRaumAbweichung()!=null)
        ? us.getRaumAbweichung().getBezeichnung()
        : u.getSchulklasse().getKlassenzimmer().getBezeichnung();

    return new PlanItemDto(
      zs.getWochentag().name(), zs.getStartStunde(), zs.getEndStunde(),
      fach, header, raumBez + " · Klasse " + klasse
    );
  }).toList();

  return new KlassenplanDto(lehrerId, header, items);
}

@Transactional(readOnly = true)
public GridPlanDto getLehrerplanGrid(Long lehrerId) {
  List<Unterrichtsstunde> stunden = ustdRepo.findAllByLehrerOrdered(lehrerId);
  String title = stunden.isEmpty()
      ? ("Lehrer #" + lehrerId)
      : (stunden.get(0).getUnterricht().getLehrer().getVorname() + " " +
         stunden.get(0).getUnterricht().getLehrer().getNachname());

  var days = new java.util.LinkedHashMap<String, java.util.List<GridCellDto>>();
  for (var us : stunden) {
    var zs = us.getZeitSlot();
    var u  = us.getUnterricht();
    String fach = u.getFach().getBezeichnung();
    String klasse = u.getSchulklasse().getBezeichnung();
    String raum = (us.getRaumAbweichung()!=null)
        ? us.getRaumAbweichung().getBezeichnung()
        : u.getSchulklasse().getKlassenzimmer().getBezeichnung();

    days.computeIfAbsent(zs.getWochentag().name(), k -> new java.util.ArrayList<>())
        .add(new GridCellDto(zs.getStartStunde(), zs.getEndStunde(), fach,
                             "Klasse " + klasse, raum));
  }
  days.values().forEach(l -> l.sort(
      java.util.Comparator.comparingInt(GridCellDto::startStunde)
                          .thenComparingInt(GridCellDto::endStunde)));
  return new GridPlanDto(lehrerId, title, days);
}

@Transactional(readOnly = true)
public GridPlanDto getRaumbelegung(Long raumId) {
  List<Unterrichtsstunde> stunden = ustdRepo.findAllByRaumOrdered(raumId);
  String title = "Raum #" + raumId;
  if (!stunden.isEmpty()) {
    title = (stunden.get(0).getRaumAbweichung()!=null)
        ? stunden.get(0).getRaumAbweichung().getBezeichnung()
        : stunden.get(0).getUnterricht().getSchulklasse().getKlassenzimmer().getBezeichnung();
  }
  var days = new java.util.LinkedHashMap<String, java.util.List<GridCellDto>>();
  for (var us : stunden) {
    var zs = us.getZeitSlot();
    var u  = us.getUnterricht();
    String fach = u.getFach().getBezeichnung();
    String sub  = u.getLehrer().getVorname() + " " + u.getLehrer().getNachname()
                  + " · Klasse " + u.getSchulklasse().getBezeichnung();
    days.computeIfAbsent(zs.getWochentag().name(), k -> new java.util.ArrayList<>())
        .add(new GridCellDto(zs.getStartStunde(), zs.getEndStunde(), fach, sub, title));
  }
  days.values().forEach(l -> l.sort(
      java.util.Comparator.comparingInt(GridCellDto::startStunde)
                          .thenComparingInt(GridCellDto::endStunde)));
  return new GridPlanDto(raumId, title, days);
}



@Transactional(readOnly = true)
public GridPlanDto getKlassenplanGrid(Long klasseId) {
  KlassenplanDto raw = getKlassenplan(klasseId);
  var byDay = new java.util.LinkedHashMap<String, java.util.List<GridCellDto>>();

  for (var item : raw.items()) {
    byDay.computeIfAbsent(item.wochentag(), k -> new java.util.ArrayList<>())
         .add(new GridCellDto(
           item.startStunde(),
           item.endStunde(),
           item.fach(),
           item.lehrer(),    
           item.raum()
         ));
  }
  
  byDay.values().forEach(list -> list.sort(java.util.Comparator
      .comparingInt(GridCellDto::startStunde)
      .thenComparingInt(GridCellDto::endStunde)));

  return new GridPlanDto(raw.klasseId(), raw.klasse(), byDay);
}


}


