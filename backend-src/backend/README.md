# 🏫 Stundenplan-Projekt

## 🚀 Überblick

Dieses Projekt bildet einen einfachen **Stundenplan für eine Schule** ab
(Fokus: Schüler-Modell). Es ermöglicht das **Anzeigen, Erstellen,
Bearbeiten und Löschen** von Unterrichtseinträgen mit integrierter
**Konfliktprüfung** (Lehrer, Klasse, Raum).

### ✨ Highlights

-   Blockbasiertes Zeitmodell (1--12 Blöcke, je 45 Minuten)\
-   Konfliktprüfung bei jeder Einplanung\
-   REST-API mit Swagger UI\
-   Zugriffsbeschränkung (GET offen · POST/PATCH/DELETE nur ADMIN)\
-   Vollständig lokal lauffähig ✅

------------------------------------------------------------------------

## 🧰 Voraussetzungen

-   **Java 21**
-   **Maven 3.9+**
-   **Docker & Docker Compose**
-   Optional: `jq` (für JSON-Ausgabe) und `curl`

------------------------------------------------------------------------

## ⚙️ Setup

### 🔹 Backend starten

``` bash
cd backend-src/backend
mvn spring-boot:run
```

**Swagger UI:** <http://localhost:8080/swagger-ui.html>\
**OpenAPI JSON:** <http://localhost:8080/v3/api-docs>

### 🔑 Demo-Benutzer

  Benutzer   Passwort   Rolle
  ---------- ---------- ---------------
  admin      admin      ROLE_ADMIN
  lehrer     lehrer     ROLE_LEHRER
  schueler   schueler   ROLE_SCHUELER

> **Hinweis:** POST / PATCH / DELETE sind nur für `admin/admin` erlaubt.

------------------------------------------------------------------------

## 🧩 Konfiguration (`application.yml`)

Pfad: `backend-src/backend/src/main/resources/application.yml`

``` yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/stundenplan
    username: sp
    password: sp
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      "[hibernate.jdbc.time_zone]": UTC
  flyway:
    enabled: true

server:
  port: 8080

stundenplan:
  blockmap:
    1:  "08:00-08:45"
    2:  "08:50-09:35"
    3:  "09:45-10:30"
    4:  "10:35-11:20"
    5:  "11:30-12:15"
    6:  "12:20-13:05"
    7:  "13:30-14:15"
    8:  "14:20-15:05"
    9:  "15:15-16:00"
    10: "16:05-16:50"
    11: "17:00-17:45"
    12: "17:50-18:35"
```

------------------------------------------------------------------------

## 🧠 Architektur-Überblick

-   Zeitmodell: Block-basiert (ZeitSlot(wochentag, startStunde,
    endStunde))
-   Konfliktprüfung: Kein Lehrer, keine Klasse, kein Raum doppelt im
    selben Slot
-   IDs: Technische PKs (bigserial), natürliche UNIQUE-Constraints
-   Sicherheit: Basic Auth (ADMIN / LEHRER / SCHUELER)
-   DTOs: Vereinfachte Objekte für API-Antworten
-   Flyway: Versionierte SQL-Migrationen (V1...V3 + Seed)

------------------------------------------------------------------------

## 🗃️ Datenmodell

  -----------------------------------------------------------------------
  Tabelle                      Beschreibung
  ---------------------------- ------------------------------------------
  lehrer                       Lehrer (id, vorname, nachname)

  schulklasse                  Schulklasse (id, bezeichnung,
                               klassenlehrer_id, klassenzimmer_id)

  schueler                     Schüler (id, vorname, nachname,
                               schulklasse_id)

  fach                         Fach (id, bezeichnung)

  zeit_slot                    Zeitblöcke (wochentag, start_stunde,
                               end_stunde)

  unterricht                   Unterricht (klasse_id, lehrer_id, fach_id)
                               --- unique(klasse, fach)

  unterrichtsstunde            Unterrichtsstunde (unterricht_id,
                               zeit_slot_id, raum_abweichung_id?)
  -----------------------------------------------------------------------

------------------------------------------------------------------------

## 🔌 REST-Endpunkte

### Öffentliche GET-Endpunkte

  Pfad                                    Beschreibung
  --------------------------------------- ---------------------------
  /api/v1/klassen                         Klassenliste
  /api/v1/klassen/{id}/stundenplan        Stundenplan einer Klasse
  /api/v1/klassen/{id}/stundenplan/grid   Stundenplan (Grid-Format)
  /api/v1/lehrer/{id}/stundenplan         Plan eines Lehrers
  /api/v1/lehrer/{id}/stundenplan/grid    Lehrerplan (Grid)
  /api/v1/raeume/{id}/belegung            Raumbelegung
  /api/v1/zeit-slots                      Zeitblöcke
  /api/v1/config/blocks                   Block → Zeit-Mapping

### Nur ADMIN (geschützt)

  -------------------------------------------------------------------------------------------
  Methode                Pfad                              Beschreibung
  ---------------------- --------------------------------- ----------------------------------
  POST                   /api/v1/unterricht                Unterricht hinzufügen

  POST                   /api/v1/unterrichtsstunden        Unterrichtsstunde hinzufügen

  PATCH                  /api/v1/unterrichtsstunden/{id}   Unterrichtsstunde verschieben

  DELETE                 /api/v1/unterrichtsstunden/{id}   Unterrichtsstunde löschen

  DELETE                 /api/v1/unterricht/{id}           Unterricht + Stunden löschen
  -------------------------------------------------------------------------------------------

### ⚠️ Konfliktmeldungen (409)

-   „Lehrer ist in diesem Slot bereits belegt."\
-   „Klasse ist in diesem Slot bereits belegt."\
-   „Raum ist in diesem Slot bereits belegt."

------------------------------------------------------------------------

## ▶️ Demo-Skript (cURL)

Pfad: `backend-src/backend/scripts/curl-demo.sh`

``` bash
chmod +x backend-src/backend/scripts/curl-demo.sh
./scripts/curl-demo.sh
```

Das Skript führt automatisch mehrere API-Aufrufe durch (Klassenliste,
Unterricht anlegen, Konflikttest usw.).

------------------------------------------------------------------------

## 🧱 Makefile (optional)

Pfad: `backend-src/backend/Makefile`

``` makefile
.PHONY: db-up db-down run reset

db-up:
  docker compose -f ../../infra/docker-compose.yml up -d

db-down:
  docker compose -f ../../infra/docker-compose.yml down -v

run:
  mvn spring-boot:run

reset: db-down db-up run
```

### Verwendung:

``` bash
cd backend-src/backend
make db-up
make run
# oder
make reset
```

------------------------------------------------------------------------

## 🎬 Demo-Szenario (5--6 Minuten)

1.  Swagger UI öffnen → `GET /klassen` (10A sichtbar)\
2.  `GET /klassen/1/stundenplan` → Startplan anzeigen\
3.  `POST /unterricht` → neues Fach/Lehrer hinzufügen (ADMIN)\
4.  `POST /unterrichtsstunden` → Zeit-Slot zuweisen\
5.  Gleicher Slot erneut → 409 Conflict\
6.  `DELETE /unterrichtsstunden/{id}` → Stunde löschen\
7.  `GET /klassen/1/stundenplan/grid` → Grid-Ansicht zeigen

------------------------------------------------------------------------

## 🧪 Fehlerbehebung

  ------------------------------------------------------------------------
  Problem                                 Lösung
  --------------------------------------- --------------------------------
  No POM in this directory                Maven im Ordner
                                          `backend-src/backend` ausführen

  stundenplan.blockmap not found          `@ConfigurationPropertiesScan`
                                          in `Application.java` prüfen

  409 CONFLICT                            Konfliktprüfung aktiv -- anderen
                                          Slot wählen

  DB/Ports blockiert                      `make reset` ausführen
  ------------------------------------------------------------------------