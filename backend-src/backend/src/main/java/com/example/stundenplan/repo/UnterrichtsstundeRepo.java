package com.example.stundenplan.repo;
import org.springframework.data.jpa.repository.*;
import com.example.stundenplan.domain.Unterrichtsstunde;
import java.util.List;
import org.springframework.data.repository.query.Param;


public interface UnterrichtsstundeRepo extends JpaRepository<Unterrichtsstunde, Long> {
  @Query("select u from Unterrichtsstunde u where u.unterricht.schulklasse.id = :klasseId")
  List<Unterrichtsstunde> findAllByKlasse(Long klasseId);

  @Query("""
    select count(us) > 0 from Unterrichtsstunde us
    where us.zeitSlot.id = :zeitSlotId
      and us.unterricht.lehrer.id = :lehrerId
  """)
  boolean existsTeacherBusy(@Param("zeitSlotId") Long zeitSlotId, @Param("lehrerId") Long lehrerId);

  @Query("""
    select count(us) > 0 from Unterrichtsstunde us
    where us.zeitSlot.id = :zeitSlotId
      and us.unterricht.schulklasse.id = :klasseId
  """)
  boolean existsClassBusy(@Param("zeitSlotId") Long zeitSlotId, @Param("klasseId") Long klasseId);
  
  @Query("""
    select count(us) > 0 from Unterrichtsstunde us
    where us.zeitSlot.id = :zeitSlotId
      and coalesce(us.raumAbweichung.id, us.unterricht.schulklasse.klassenzimmer.id) = :raumId
  """)
  boolean existsRoomBusy(@Param("zeitSlotId") Long zeitSlotId, @Param("raumId") Long raumId);

  @Query("""
  select u from Unterrichtsstunde u
  where u.unterricht.lehrer.id = :lehrerId
  order by u.zeitSlot.wochentag, u.zeitSlot.startStunde, u.zeitSlot.endStunde
""")
List<Unterrichtsstunde> findAllByLehrerOrdered(@Param("lehrerId") Long lehrerId);
  
  @Query("""
  select u from Unterrichtsstunde u
  where coalesce(u.raumAbweichung.id, u.unterricht.schulklasse.klassenzimmer.id) = :raumId
  order by u.zeitSlot.wochentag, u.zeitSlot.startStunde, u.zeitSlot.endStunde
""")
List<Unterrichtsstunde> findAllByRaumOrdered(@Param("raumId") Long raumId);

  
}
