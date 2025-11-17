-- H:\SA-backnd\Stundenplan\backend-src\backend\src\main\resources\db\migration\V2__school_model.sql

-- === Persons ===
create table if not exists lehrer (
  id bigserial primary key,
  vorname text not null,
  nachname text not null
);

create table if not exists schueler (
  id bigserial primary key,
  vorname text not null,
  nachname text not null,
  schulklasse_id bigint not null
);

-- === Cern (Core Entities) ===
create table if not exists fach (
  id bigserial primary key,
  bezeichnung text not null unique
);

create table if not exists schulklasse (
  id bigserial primary key,
  bezeichnung text not null unique,
  klassenlehrer_id bigint not null,
  klassenzimmer_id bigint not null
);

-- === Relationships (FK's) ===
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_schueler_klasse') THEN
    ALTER TABLE schueler
      ADD CONSTRAINT fk_schueler_klasse
      FOREIGN KEY (schulklasse_id) REFERENCES schulklasse(id) ON DELETE CASCADE;
  END IF;
END$$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_klasse_lehrer') THEN
    ALTER TABLE schulklasse
      ADD CONSTRAINT fk_klasse_lehrer
      FOREIGN KEY (klassenlehrer_id) REFERENCES lehrer(id);
  END IF;

  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_klasse_raum') THEN
    ALTER TABLE schulklasse
      ADD CONSTRAINT fk_klasse_raum
      FOREIGN KEY (klassenzimmer_id) REFERENCES raum(id);
  END IF;
END$$;


-- === Lecture/Time ===

create table if not exists unterricht (
  id bigserial primary key,
  schulklasse_id bigint not null references schulklasse(id) on delete cascade,
  lehrer_id      bigint not null references lehrer(id),
  fach_id        bigint not null references fach(id),
  

  unique (schulklasse_id, fach_id, lehrer_id)
);


create table if not exists unterrichtsstunde (
  id bigserial primary key,
  unterricht_id      bigint not null references unterricht(id) on delete cascade,
  zeit_slot_id       bigint not null references zeit_slot(id),
  raum_abweichung_id bigint references raum(id)
);



-- === INDEXES (Jetzt am Ende - korrekt) ===
create index if not exists idx_schueler_klasse      on schueler(schulklasse_id);
create index if not exists idx_unterricht_klasse      on unterricht(schulklasse_id);
create index if not exists idx_unterrichtsstunde_slot on unterrichtsstunde(zeit_slot_id);