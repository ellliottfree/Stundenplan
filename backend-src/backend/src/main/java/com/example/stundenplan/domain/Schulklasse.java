package com.example.stundenplan.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "klassenlehrer_id")
    @JsonIgnore
    private Lehrer klassenlehrer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "klassenzimmer_id")
    @JsonIgnore
    private Raum klassenzimmer;
}
