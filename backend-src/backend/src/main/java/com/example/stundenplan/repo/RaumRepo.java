package com.example.stundenplan.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.stundenplan.domain.Raum;
public interface RaumRepo extends JpaRepository<Raum, Long> {}

