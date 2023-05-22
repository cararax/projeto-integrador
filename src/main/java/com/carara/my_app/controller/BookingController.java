package com.carara.my_app.controller;

import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.domain.Carerecivier;
import com.carara.my_app.domain.Service;
import com.carara.my_app.model.BookingDTO;
import com.carara.my_app.repos.CaregiverRepository;
import com.carara.my_app.repos.CarerecivierRepository;
import com.carara.my_app.repos.ServiceRepository;
import com.carara.my_app.service.BookingService;
import com.carara.my_app.util.CustomCollectors;
import com.carara.my_app.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final CaregiverRepository caregiverRepository;
    private final CarerecivierRepository carerecivierRepository;
    private final ServiceRepository serviceRepository;

    public BookingController(final BookingService bookingService,
            final CaregiverRepository caregiverRepository,
            final CarerecivierRepository carerecivierRepository,
            final ServiceRepository serviceRepository) {
        this.bookingService = bookingService;
        this.caregiverRepository = caregiverRepository;
        this.carerecivierRepository = carerecivierRepository;
        this.serviceRepository = serviceRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("caregiverValues", caregiverRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Caregiver::getId, Caregiver::getFirstname)));
        model.addAttribute("carerecivierValues", carerecivierRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Carerecivier::getId, Carerecivier::getFirstname)));
        model.addAttribute("servicesValues", serviceRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Service::getId, Service::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("bookings", bookingService.findAll());
        return "booking/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("booking") final BookingDTO bookingDTO) {
        return "booking/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("booking") @Valid final BookingDTO bookingDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "booking/add";
        }
        bookingService.create(bookingDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("booking.create.success"));
        return "redirect:/bookings";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("booking", bookingService.get(id));
        return "booking/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("booking") @Valid final BookingDTO bookingDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "booking/edit";
        }
        bookingService.update(id, bookingDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("booking.update.success"));
        return "redirect:/bookings";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        bookingService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("booking.delete.success"));
        return "redirect:/bookings";
    }

}
