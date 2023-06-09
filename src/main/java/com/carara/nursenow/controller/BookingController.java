package com.carara.nursenow.controller;

import com.carara.nursenow.domain.Service;
import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.BookingDTO;
import com.carara.nursenow.model.ROLE;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.repos.ServiceRepository;
import com.carara.nursenow.repos.UsersRepository;
import com.carara.nursenow.service.BookingService;
import com.carara.nursenow.util.CustomCollectors;
import com.carara.nursenow.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/bookings")
@PreAuthorize("hasAuthority('" + ROLE.Fields.CAREGIVER + "')")
public class BookingController {

    private final BookingService bookingService;
    private final UsersRepository usersRepository;
    private final ServiceRepository serviceRepository;

    public BookingController(final BookingService bookingService,
            final UsersRepository usersRepository, final ServiceRepository serviceRepository) {
        this.bookingService = bookingService;
        this.usersRepository = usersRepository;
        this.serviceRepository = serviceRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("caregiverValues", usersRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Users::getId, Users::getFirstname)));
        model.addAttribute("carerecivierValues", usersRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Users::getId, Users::getFirstname)));
        model.addAttribute("servicesValues", serviceRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Service::getId, Service::getName)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<BookingDTO> bookings = bookingService.findAll(filter, pageable);
        model.addAttribute("bookings", bookings);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(bookings));
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
