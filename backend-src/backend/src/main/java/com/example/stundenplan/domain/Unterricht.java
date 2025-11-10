package com.example.stundenplan.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "unterricht",
       uniqueConstraints = @UniqueConstraint(columnNames = {"schulklasse_id", "fach_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Unterricht {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "schulklasse_id")
    @JsonIgnore
    private Schulklasse schulklasse;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "lehrer_id")
    @JsonIgnore
    private Lehrer lehrer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fach_id")
    @JsonIgnore
    private Fach fach;
    
    @Builder.Default
    @OneToMany(mappedBy="unterricht", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference
    private Set<Unterrichtsstunde> stunden = new LinkedHashSet<>();
}
