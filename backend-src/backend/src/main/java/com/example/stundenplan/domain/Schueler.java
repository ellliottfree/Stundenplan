package com.example.stundenplan.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "schueler")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Schueler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vorname;

    @Column(nullable = false)
    private String nachname;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "schulklasse_id")
    @JsonIgnore
    private Schulklasse schulklasse;
}

