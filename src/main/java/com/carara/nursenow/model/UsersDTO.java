package com.carara.nursenow.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class UsersDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String firstname;

    @NotNull
    @Size(max = 255)
    private String lastname;

    @NotNull
    @Size(max = 255)
    private String email;

    private String password;

    private ROLE role;

    private String description;

    @Size(max = 255)
    private String elderyName;

    private String healthDetails;

    private LocalDate elderyBirthDate;

    private Long city;

}
