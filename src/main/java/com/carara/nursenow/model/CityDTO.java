package com.carara.nursenow.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CityDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

}
