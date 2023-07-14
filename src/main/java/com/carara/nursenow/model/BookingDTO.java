package com.carara.nursenow.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


@Getter
@Setter
public class BookingDTO {

    private Long id;

//    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
//    @DateTimeFormat(pattern = "yyyy-MM-dd") //'T'HH:mm:ssXXX")
    private LocalDateTime startDateTime;

//    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private LocalDateTime endDateTime;

    private Long caregiver;

    private Long carerecivier;

    private Long services;

}
