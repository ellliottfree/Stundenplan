package com.example.stundenplan.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.stundenplan.domain.Schulklasse;
public interface SchulklasseRepo extends JpaRepository<Schulklasse, Long> {}
