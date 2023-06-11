package com.carara.nursenow.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ExperienceDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String company;

    private String description;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private Long caregiver;

}
