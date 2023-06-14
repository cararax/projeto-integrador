package com.carara.nursenow.domain;

import com.carara.nursenow.model.ROLE;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Users {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ROLE role;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String elderyName;

    @Column(columnDefinition = "text")
    private String healthDetails;

    @Column
    private LocalDate elderyBirthDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "caregiver")
    private Set<Experience> experience;

    @OneToMany(fetch = FetchType.EAGER ,mappedBy = "caregiver")
    private Set<Service> service;

}
