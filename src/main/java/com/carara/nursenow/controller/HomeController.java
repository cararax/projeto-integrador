package com.carara.nursenow.controller;

import com.carara.nursenow.domain.Booking;
import com.carara.nursenow.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class HomeController {

    @Autowired
    BookingService bookingService;

    @GetMapping("/")
    public String index() {
        return "home/index";
    }

    @GetMapping("/TESTE")
    public String index2(final Model model) {
        long caregiverId = 1001L;
        List<Booking> futureBookingsByCarerecivierId = bookingService.findByCaregiverIdAndStartDateTimeAfter(caregiverId);
        List<Booking> pastBookingsByCarerecivierId = bookingService.findByCaregiverIdAndEndDateTimeBefore(caregiverId);

        model.addAttribute("bookings", bookingService.findAll(null, PageRequest.of(0, 10)));
        model.addAttribute("futureBookings",futureBookingsByCarerecivierId);
        model.addAttribute("pastBookings",pastBookingsByCarerecivierId);
        return "home/teste";
    }

}
