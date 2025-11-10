package com.example.stundenplan.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "zeit_slot",
       uniqueConstraints = @UniqueConstraint(columnNames = {"wochentag", "start_stunde", "end_stunde"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ZeitSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Wochentag wochentag;

    @Min(1) @Max(12)
    @Column(name = "start_stunde", nullable = false)
    private Integer startStunde;

    @Min(1) @Max(12)
    @Column(name = "end_stunde", nullable = false)
    private Integer endStunde;
}
