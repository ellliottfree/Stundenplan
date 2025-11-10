package com.example.stundenplan.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.stundenplan.domain.Schueler;
import java.util.List;
public interface SchuelerRepo extends JpaRepository<Schueler, Long> {
  List<Schueler> findBySchulklasseId(Long schulklasseId);
}
