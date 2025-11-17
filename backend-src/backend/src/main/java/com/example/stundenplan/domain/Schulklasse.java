package com.example.stundenplan.domain;

// Wir brauchen @JsonIgnore nicht mehr, da wir EAGER Fetching verwenden
// import com.fasterxml.jackson.annotation.JsonIgnore; 

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "schulklasse", uniqueConstraints = @UniqueConstraint(columnNames = "bezeichnung"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Schulklasse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bezeichnung;


    @ManyToOne(optional = false, fetch = FetchType.EAGER) // Von LAZY auf EAGER geändert
    @JoinColumn(name = "klassenlehrer_id")
    private Lehrer klassenlehrer;


    @ManyToOne(optional = false, fetch = FetchType.EAGER) // Von LAZY auf EAGER geändert
    @JoinColumn(name = "klassenzimmer_id")
    private Raum klassenzimmer;
}