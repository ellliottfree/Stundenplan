package com.example.stundenplan.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "raum", uniqueConstraints = @UniqueConstraint(columnNames = "bezeichnung"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Raum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String bezeichnung;

    @Min(1)
    @Column(nullable = false)
    private Integer kapazitaet;
}
