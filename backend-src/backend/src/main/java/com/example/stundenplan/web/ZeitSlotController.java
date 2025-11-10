package com.example.stundenplan.web;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.stundenplan.repo.ZeitSlotRepo;
import com.example.stundenplan.domain.ZeitSlot;

@RestController
@RequestMapping("/api/v1/zeit-slots")
public class ZeitSlotController {
  private final ZeitSlotRepo repo;
  public ZeitSlotController(ZeitSlotRepo repo){ this.repo = repo; }

  @GetMapping
  public List<ZeitSlot> all(){ return repo.findAll(); }
}

