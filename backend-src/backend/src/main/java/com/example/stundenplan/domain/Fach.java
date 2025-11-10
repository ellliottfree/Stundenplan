package com.example.stundenplan.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fach", uniqueConstraints = @UniqueConstraint(columnNames = "bezeichnung"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Fach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bezeichnung;
}
