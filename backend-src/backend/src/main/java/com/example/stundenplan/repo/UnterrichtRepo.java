package com.example.stundenplan.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.stundenplan.domain.Unterricht;
import java.util.List;
public interface UnterrichtRepo extends JpaRepository<Unterricht, Long> {
  boolean existsBySchulklasseIdAndFachId(Long klasseId, Long fachId);
  List<Unterricht> findBySchulklasseId(Long klasseId);
}
