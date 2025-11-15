package com.example.stundenplan.web;

import com.example.stundenplan.domain.Raum;
import com.example.stundenplan.repo.RaumRepo;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/raeume")
@RequiredArgsConstructor
public class RaumController {
    private final RaumRepo repo;

    @GetMapping
    public List<Raum> getAll() {
        return repo.findAll();
    }
}

