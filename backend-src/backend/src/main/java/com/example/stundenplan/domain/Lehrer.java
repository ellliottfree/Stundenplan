package com.example.stundenplan.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lehrer")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Lehrer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vorname;

    @Column(nullable = false)
    private String nachname;
}
