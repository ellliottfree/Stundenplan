package com.example.stundenplan.service;

import com.example.stundenplan.domain.*;
import com.example.stundenplan.repo.*;
import com.example.stundenplan.web.ConflictException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduleService {
  private final SchulklasseRepo klasseRepo;
  private final FachRepo fachRepo;
  private final LehrerRepo lehrerRepo;
  private final UnterrichtRepo unterrichtRepo;
  private final UnterrichtsstundeRepo ustdRepo;
  private final ZeitSlotRepo zeitSlotRepo;
  private final RaumRepo raumRepo;

  public ScheduleService(SchulklasseRepo klasseRepo, FachRepo fachRepo, LehrerRepo lehrerRepo,
                         UnterrichtRepo unterrichtRepo, UnterrichtsstundeRepo ustdRepo,
                         ZeitSlotRepo zeitSlotRepo, RaumRepo raumRepo) {
    this.klasseRepo = klasseRepo; this.fachRepo = fachRepo; this.lehrerRepo = lehrerRepo;
    this.unterrichtRepo = unterrichtRepo; this.ustdRepo = ustdRepo;
    this.zeitSlotRepo = zeitSlotRepo; this.raumRepo = raumRepo;
  }

  @Transactional
  public Unterricht addUnterricht(Long klasseId, Long fachId, Long lehrerId) {
    if (unterrichtRepo.existsBySchulklasseIdAndFachId(klasseId, fachId)) {
      throw new ConflictException("In dieser Klasse existiert das Fach bereits (ein Lehrer ist schon zugewiesen).");
    }
    var klasse = klasseRepo.findById(klasseId).orElseThrow(() -> new IllegalArgumentException("Klasse nicht gefunden"));
    var fach   = fachRepo.findById(fachId).orElseThrow(() -> new IllegalArgumentException("Fach nicht gefunden"));
    var lehrer = lehrerRepo.findById(lehrerId).orElseThrow(() -> new IllegalArgumentException("Lehrer nicht gefunden"));

    var u = Unterricht.builder().schulklasse(klasse).fach(fach).lehrer(lehrer).build();
    return unterrichtRepo.save(u);
  }

  @Transactional
public void deleteUnterricht(Long unterrichtId) {
  if (!unterrichtRepo.existsById(unterrichtId)) {
    throw new IllegalArgumentException("Unterricht nicht gefunden");
  }
  unterrichtRepo.deleteById(unterrichtId); 
}

@Transactional
public void deleteUnterrichtsstunde(Long id) {
  if (!ustdRepo.existsById(id)) {
    throw new IllegalArgumentException("Unterrichtsstunde nicht gefunden");
  }
  ustdRepo.deleteById(id);
}

@Transactional
public Unterrichtsstunde moveUnterrichtsstunde(Long id, Long newZeitSlotId, Long newRaumAbweichungId) {
  var st = ustdRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Unterrichtsstunde nicht gefunden"));
  var u  = st.getUnterricht();
  var zs = zeitSlotRepo.findById(newZeitSlotId).orElseThrow(() -> new IllegalArgumentException("ZeitSlot nicht gefunden"));

  Long klasseId = u.getSchulklasse().getId();
  Long lehrerId = u.getLehrer().getId();
  Long raumId   = (newRaumAbweichungId != null)
      ? newRaumAbweichungId
      : u.getSchulklasse().getKlassenzimmer().getId();

  // Um ​​Selbstkonflikte zu vermeiden: Falls keine alternative Abfrage existiert, die den aktuellen Datensatz ausschließt,
  // wird die Prüfung fortgesetzt, ohne den alten Datensatz vorübergehend zu löschen. Ändert sich jedoch der Slot,
  // kann auch der Slot des aktuellen Datensatzes als potenziell konfliktbehaftet betrachtet werden.
  // Praktische Lösung: Wird der Datensatz in denselben Slot verschoben, ist er zulässig; wird er in einen anderen Slot verschoben, wird er geprüft.
  boolean slotChanged = !st.getZeitSlot().getId().equals(newZeitSlotId);
  boolean roomChanged = (st.getRaumAbweichung()==null ? null : st.getRaumAbweichung().getId()) != newRaumAbweichungId;

  if (slotChanged) {
    if (ustdRepo.existsTeacherBusy(zs.getId(), lehrerId))
      throw new ConflictException("Lehrer ist in diesem Slot bereits belegt.");
    if (ustdRepo.existsClassBusy(zs.getId(), klasseId))
      throw new ConflictException("Klasse ist in diesem Slot bereits belegt.");
    if (ustdRepo.existsRoomBusy(zs.getId(), raumId))
      throw new ConflictException("Raum ist in diesem Slot bereits belegt.");
  } else if (roomChanged) {
    // sadece oda değişmişse oda çakışmasını kontrol et
    if (ustdRepo.existsRoomBusy(zs.getId(), raumId))
      throw new ConflictException("Raum ist in diesem Slot bereits belegt.");
  }

  st.setZeitSlot(zs);
  if (newRaumAbweichungId == null) {
    st.setRaumAbweichung(null);
  } else {
    var raum = raumRepo.findById(newRaumAbweichungId)
        .orElseThrow(() -> new IllegalArgumentException("Raum (Abweichung) nicht gefunden"));
    st.setRaumAbweichung(raum);
  }
  return ustdRepo.save(st);
}


  @Transactional
  public Unterrichtsstunde addUnterrichtsstunde(Long unterrichtId, Long zeitSlotId, Long raumAbweichungId) {
    var u  = unterrichtRepo.findById(unterrichtId).orElseThrow(() -> new IllegalArgumentException("Unterricht nicht gefunden"));
    var zs = zeitSlotRepo.findById(zeitSlotId).orElseThrow(() -> new IllegalArgumentException("ZeitSlot nicht gefunden"));

    // Konfliktprüfung
    Long klasseId = u.getSchulklasse().getId();
    Long lehrerId = u.getLehrer().getId();
    Long raumId   = (raumAbweichungId != null)
            ? raumAbweichungId
            : u.getSchulklasse().getKlassenzimmer().getId();

    if (ustdRepo.existsTeacherBusy(zs.getId(), lehrerId))
      throw new ConflictException("Lehrer ist in diesem Slot bereits belegt.");
    if (ustdRepo.existsClassBusy(zs.getId(), klasseId))
      throw new ConflictException("Klasse ist in diesem Slot bereits belegt.");
    if (ustdRepo.existsRoomBusy(zs.getId(), raumId))
      throw new ConflictException("Raum ist in diesem Slot bereits belegt.");

    Raum abw = (raumAbweichungId == null) ? null : raumRepo.findById(raumAbweichungId)
            .orElseThrow(() -> new IllegalArgumentException("Raum (Abweichung) nicht gefunden"));

    var st = Unterrichtsstunde.builder()
            .unterricht(u)
            .zeitSlot(zs)
            .raumAbweichung(abw)
            .build();

    return ustdRepo.save(st);
  }
}

