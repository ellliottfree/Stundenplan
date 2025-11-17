-- V3__seed.sql
-- (Version 2: Nur noch Einzelstunden-Blöcke)

-- 1. Basisdaten (Lehrer, Räume, Fächer)
INSERT INTO lehrer(vorname, nachname) VALUES
('Peter', 'Müller'),
('Sabine', 'Schmidt'),
('Michael', 'Weber'),
('Laura', 'Schneider')
ON CONFLICT DO NOTHING;

INSERT INTO raum(bezeichnung, kapazitaet) VALUES
('R101', 30),
('R102', 30),
('R201 (Labor)', 24),
('Turnhalle', 50)
ON CONFLICT (bezeichnung) DO NOTHING;

INSERT INTO fach(bezeichnung) VALUES
('Mathematik'),
('Physik'),
('Deutsch'),
('Englisch'),
('Sport'),
('Kunst')
ON CONFLICT (bezeichnung) DO NOTHING;

-- 2. Zeit-Slots (Wochentage + Blöcke)
-- --- MODIFIKATION HIER (WIR ERSTELLEN 60 EINZELSTUNDEN) ---
INSERT INTO zeit_slot(wochentag, start_stunde, end_stunde) VALUES
('MONTAG', 1, 1), ('MONTAG', 2, 2), ('MONTAG', 3, 3), ('MONTAG', 4, 4), ('MONTAG', 5, 5), ('MONTAG', 6, 6), ('MONTAG', 7, 7), ('MONTAG', 8, 8), ('MONTAG', 9, 9), ('MONTAG', 10, 10), ('MONTAG', 11, 11), ('MONTAG', 12, 12),
('DIENSTAG', 1, 1), ('DIENSTAG', 2, 2), ('DIENSTAG', 3, 3), ('DIENSTAG', 4, 4), ('DIENSTAG', 5, 5), ('DIENSTAG', 6, 6), ('DIENSTAG', 7, 7), ('DIENSTAG', 8, 8), ('DIENSTAG', 9, 9), ('DIENSTAG', 10, 10), ('DIENSTAG', 11, 11), ('DIENSTAG', 12, 12),
('MITTWOCH', 1, 1), ('MITTWOCH', 2, 2), ('MITTWOCH', 3, 3), ('MITTWOCH', 4, 4), ('MITTWOCH', 5, 5), ('MITTWOCH', 6, 6), ('MITTWOCH', 7, 7), ('MITTWOCH', 8, 8), ('MITTWOCH', 9, 9), ('MITTWOCH', 10, 10), ('MITTWOCH', 11, 11), ('MITTWOCH', 12, 12),
('DONNERSTAG', 1, 1), ('DONNERSTAG', 2, 2), ('DONNERSTAG', 3, 3), ('DONNERSTAG', 4, 4), ('DONNERSTAG', 5, 5), ('DONNERSTAG', 6, 6), ('DONNERSTAG', 7, 7), ('DONNERSTAG', 8, 8), ('DONNERSTAG', 9, 9), ('DONNERSTAG', 10, 10), ('DONNERSTAG', 11, 11), ('DONNERSTAG', 12, 12),
('FREITAG', 1, 1), ('FREITAG', 2, 2), ('FREITAG', 3, 3), ('FREITAG', 4, 4), ('FREITAG', 5, 5), ('FREITAG', 6, 6), ('FREITAG', 7, 7), ('FREITAG', 8, 8), ('FREITAG', 9, 9), ('FREITAG', 10, 10), ('FREITAG', 11, 11), ('FREITAG', 12, 12)
ON CONFLICT (wochentag, start_stunde, end_stunde) DO NOTHING;


-- 3. Schulklassen (3 Klassen)
INSERT INTO schulklasse(bezeichnung, klassenlehrer_id, klassenzimmer_id) VALUES
( '10A', (SELECT id FROM lehrer WHERE vorname='Peter' AND nachname='Müller'), (SELECT id FROM raum WHERE bezeichnung='R101') ),
( '10B', (SELECT id FROM lehrer WHERE vorname='Sabine' AND nachname='Schmidt'), (SELECT id FROM raum WHERE bezeichnung='R102') ),
( '11A', (SELECT id FROM lehrer WHERE vorname='Laura' AND nachname='Schneider'), (SELECT id FROM raum WHERE bezeichnung='R201 (Labor)') )
ON CONFLICT (bezeichnung) DO NOTHING;


-- 4. Schueler
INSERT INTO schueler(vorname, nachname, schulklasse_id) VALUES
('Max', 'Mustermann', (SELECT id FROM schulklasse WHERE bezeichnung='10A')),
('Lisa', 'Meyer', (SELECT id FROM schulklasse WHERE bezeichnung='10A')),
('Tom', 'Schmidt', (SELECT id FROM schulklasse WHERE bezeichnung='10B'))
ON CONFLICT DO NOTHING;


-- 5. Unterricht (Beziehungen)
INSERT INTO unterricht(schulklasse_id, fach_id, lehrer_id) VALUES
((SELECT id FROM schulklasse WHERE bezeichnung='10A'), (SELECT id FROM fach WHERE bezeichnung='Mathematik'), (SELECT id FROM lehrer WHERE vorname='Peter' AND nachname='Müller')),
((SELECT id FROM schulklasse WHERE bezeichnung='10A'), (SELECT id FROM fach WHERE bezeichnung='Deutsch'), (SELECT id FROM lehrer WHERE vorname='Sabine' AND nachname='Schmidt')),
((SELECT id FROM schulklasse WHERE bezeichnung='10A'), (SELECT id FROM fach WHERE bezeichnung='Sport'), (SELECT id FROM lehrer WHERE vorname='Michael' AND nachname='Weber')),
((SELECT id FROM schulklasse WHERE bezeichnung='10B'), (SELECT id FROM fach WHERE bezeichnung='Physik'), (SELECT id FROM lehrer WHERE vorname='Sabine' AND nachname='Schmidt')),
((SELECT id FROM schulklasse WHERE bezeichnung='10B'), (SELECT id FROM fach WHERE bezeichnung='Englisch'), (SELECT id FROM lehrer WHERE vorname='Laura' AND nachname='Schneider'))
ON CONFLICT (schulklasse_id, fach_id, lehrer_id) DO NOTHING;


-- 6. Unterrichtsstunden (Der finale Stundenplan)
-- --- MODIFIKATION HIER (WIR BUCHEN DOPPELSTUNDEN ALS ZWEI EINZELSTUNDEN) ---
INSERT INTO unterrichtsstunde(unterricht_id, zeit_slot_id, raum_abweichung_id) VALUES
(
  -- 10A Mathe (Müller) @ Mo 1. Stunde
  (SELECT u.id FROM unterricht u JOIN schulklasse k ON u.schulklasse_id = k.id JOIN fach f ON u.fach_id = f.id WHERE k.bezeichnung='10A' AND f.bezeichnung='Mathematik'),
  (SELECT z.id FROM zeit_slot z WHERE z.wochentag='MONTAG' AND z.start_stunde=1 AND z.end_stunde=1),
  NULL 
),
(
  -- 10A Mathe (Müller) @ Mo 2. Stunde
  (SELECT u.id FROM unterricht u JOIN schulklasse k ON u.schulklasse_id = k.id JOIN fach f ON u.fach_id = f.id WHERE k.bezeichnung='10A' AND f.bezeichnung='Mathematik'),
  (SELECT z.id FROM zeit_slot z WHERE z.wochentag='MONTAG' AND z.start_stunde=2 AND z.end_stunde=2),
  NULL
),
(
  -- 10A Deutsch (Schmidt) @ Di 1. Stunde
  (SELECT u.id FROM unterricht u JOIN schulklasse k ON u.schulklasse_id = k.id JOIN fach f ON u.fach_id = f.id WHERE k.bezeichnung='10A' AND f.bezeichnung='Deutsch'),
  (SELECT z.id FROM zeit_slot z WHERE z.wochentag='DIENSTAG' AND z.start_stunde=1 AND z.end_stunde=1),
  NULL
),
(
  -- 10A Deutsch (Schmidt) @ Di 2. Stunde
  (SELECT u.id FROM unterricht u JOIN schulklasse k ON u.schulklasse_id = k.id JOIN fach f ON u.fach_id = f.id WHERE k.bezeichnung='10A' AND f.bezeichnung='Deutsch'),
  (SELECT z.id FROM zeit_slot z WHERE z.wochentag='DIENSTAG' AND z.start_stunde=2 AND z.end_stunde=2),
  NULL
),
(
  -- 10A Sport (Weber) @ Fr 3. Stunde (in der Turnhalle)
  (SELECT u.id FROM unterricht u JOIN schulklasse k ON u.schulklasse_id = k.id JOIN fach f ON u.fach_id = f.id WHERE k.bezeichnung='10A' AND f.bezeichnung='Sport'),
  (SELECT z.id FROM zeit_slot z WHERE z.wochentag='FREITAG' AND z.start_stunde=3 AND z.end_stunde=3),
  (SELECT r.id FROM raum r WHERE r.bezeichnung='Turnhalle') -- Raum-Abweichung
)
ON CONFLICT DO NOTHING;