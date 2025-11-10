package com.example.stundenplan.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "unterrichtsstunde")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Unterrichtsstunde {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="unterricht_id")
    @JsonBackReference
    private Unterricht unterricht;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "zeit_slot_id")
    @JsonIgnore
    private ZeitSlot zeitSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raum_abweichung_id")
    @JsonIgnore
    private Raum raumAbweichung; // null → Das Klassenzimmer der Klasse wird benutzt
}

