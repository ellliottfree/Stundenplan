package com.example.stundenplan.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.stundenplan.repo.*;
import com.example.stundenplan.domain.*;
import org.springframework.lang.NonNull;

@RestController
@RequestMapping("/api/v1/raeume") 
public class RaumController {

    private final RaumRepo repo;
    
    public RaumController(RaumRepo r) {
        this.repo = r;
    }

    @GetMapping
    public List<Raum> getAllRaeume() {
        return repo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Raum createRaum(@RequestBody @NonNull Raum raum) {
        return repo.save(raum);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRaum(@PathVariable Long id) {
        repo.deleteById(id);
    }
}