package com.example.stundenplan.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.stundenplan.domain.ZeitSlot;
public interface ZeitSlotRepo extends JpaRepository<ZeitSlot, Long> {}

