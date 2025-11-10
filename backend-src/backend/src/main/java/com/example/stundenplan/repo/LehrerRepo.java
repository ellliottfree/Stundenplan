package com.example.stundenplan.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.stundenplan.domain.Lehrer;
public interface LehrerRepo extends JpaRepository<Lehrer, Long> {}
