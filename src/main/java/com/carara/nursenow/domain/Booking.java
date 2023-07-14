package com.carara.nursenow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@Entity
@Getter
@Setter
public class Booking {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "caregiver_id")
    private Users caregiver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carerecivier_id")
    private Users carerecivier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "services_id")
    private Service services;

}
