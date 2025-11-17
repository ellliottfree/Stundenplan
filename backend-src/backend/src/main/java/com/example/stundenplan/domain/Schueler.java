package com.example.stundenplan.domain;

import jakarta.persistence.*;
import lombok.*;
// import com.fasterxml.jackson.annotation.JsonIgnore; // Nicht mehr benötigt

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

    // Wir müssen die Klasse EAGER laden (sofort laden)
    // und @JsonIgnore entfernen
    @ManyToOne(optional = false, fetch = FetchType.EAGER) // War LAZY
    @JoinColumn(name = "schulklasse_id")
    // @JsonIgnore // Entfernt
    private Schulklasse schulklasse;
}
