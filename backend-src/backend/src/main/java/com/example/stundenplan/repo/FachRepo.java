package com.example.stundenplan.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.stundenplan.domain.Fach;
public interface FachRepo extends JpaRepository<Fach, Long> {}
