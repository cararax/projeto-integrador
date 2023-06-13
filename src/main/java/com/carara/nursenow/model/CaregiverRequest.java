package com.carara.nursenow.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CaregiverRequest {

    @NotNull
    @Size(max = 255)
    private String firstname;

    @NotNull
    @Size(max = 255)
    private String lastname;

    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    private ROLE role;

    private String description;

}
