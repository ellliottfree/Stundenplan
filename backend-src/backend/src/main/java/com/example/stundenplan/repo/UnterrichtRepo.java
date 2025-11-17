// backend-src/backend/src/main/java/com/example/stundenplan/repo/UnterrichtRepo.java

package com.example.stundenplan.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.stundenplan.domain.Unterricht;
import java.util.List;
import java.util.Optional; 

public interface UnterrichtRepo extends JpaRepository<Unterricht, Long> {
    
    boolean existsBySchulklasseIdAndFachId(Long klasseId, Long fachId); // (Die alte, fehlerhafte Methode)
    
    List<Unterricht> findBySchulklasseId(Long klasseId);

    
    // Findet eine Beziehung basierend auf allen DREI Schlüsseln
    Optional<Unterricht> findBySchulklasseIdAndFachIdAndLehrerId(Long klasseId, Long fachId, Long lehrerId);
}