package com.carara.my_app.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CarerecivierDTO {

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

    @NotNull
    @Size(max = 255)
    private String password;

    private String description;

    @NotNull
    @Size(max = 255)
    private String elderyName;

    @NotNull
    private LocalDate elderyBithDate;

    private String healthDetails;

    @NotNull
    private Role role;

    private Long city;

}
