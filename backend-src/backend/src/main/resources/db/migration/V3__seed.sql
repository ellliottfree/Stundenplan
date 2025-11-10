-- Rooms & time slots
insert into raum(bezeichnung, kapazitaet) values
  ('R101', 30), ('Lab1', 20)
on conflict do nothing;

insert into zeit_slot(wochentag, start_stunde, end_stunde) values
  ('MONTAG',1,2),('MONTAG',3,4),('DIENSTAG',1,2)
on conflict do nothing;

-- Lehrer, Fächer
insert into lehrer(vorname, nachname) values
  ('Gökhan','Bayrakci'), ('Yirui','Fu')
on conflict do nothing;

insert into fach(bezeichnung) values
  ('Mathematik'),('Physik')
on conflict do nothing;

-- Schulklasse
insert into schulklasse(bezeichnung, klassenlehrer_id, klassenzimmer_id)
values (
  '10A',
  (select id from lehrer order by id limit 1),
  (select id from raum where bezeichnung='R101')
)
on conflict do nothing;

-- Schueler
insert into schueler(vorname, nachname, schulklasse_id) values
  ('Lana','Del Rey',(select id from schulklasse where bezeichnung='10A')),
  ('Taylor','Swift',(select id from schulklasse where bezeichnung='10A'))
on conflict do nothing;

-- Unterricht (10A Mathematik)
insert into unterricht(schulklasse_id, lehrer_id, fach_id)
values (
  (select id from schulklasse where bezeichnung='10A'),
  (select id from lehrer order by id limit 1),
  (select id from fach where bezeichnung='Mathematik')
)
on conflict do nothing;

-- Unterrichtsstunde: MONTAG 1-2
insert into unterrichtsstunde(unterricht_id, zeit_slot_id, raum_abweichung_id)
values (
  (select u.id from unterricht u
    join schulklasse k on u.schulklasse_id=k.id
    join fach f on u.fach_id=f.id
   where k.bezeichnung='10A' and f.bezeichnung='Mathematik'),
  (select id from zeit_slot where wochentag='MONTAG' and start_stunde=1 and end_stunde=2),
  null
);
