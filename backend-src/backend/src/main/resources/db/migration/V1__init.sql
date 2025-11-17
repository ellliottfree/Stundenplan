
create table if not exists raum (
  id bigserial primary key,
  bezeichnung text not null unique,
  kapazitaet int not null check (kapazitaet > 0)
);

create table if not exists zeit_slot (
  id bigserial primary key,
  wochentag text not null,
  start_stunde int not null check (start_stunde between 1 and 12),
  end_stunde   int not null check (end_stunde between 1 and 12),
  unique (wochentag, start_stunde, end_stunde),
  check (end_stunde >= start_stunde)
);
