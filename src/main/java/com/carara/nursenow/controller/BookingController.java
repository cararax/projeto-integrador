package com.carara.nursenow.controller;

import com.carara.nursenow.domain.Booking;
import com.carara.nursenow.domain.Service;
import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.BookingDTO;
import com.carara.nursenow.model.HttpUserDetails;
import com.carara.nursenow.model.ROLE;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.repos.CityRepository;
import com.carara.nursenow.repos.ServiceRepository;
import com.carara.nursenow.repos.UsersRepository;
import com.carara.nursenow.service.BookingService;
import com.carara.nursenow.service.UsersService;
import com.carara.nursenow.util.CustomCollectors;
import com.carara.nursenow.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/bookings")
@PreAuthorize("hasAuthority('" + ROLE.Fields.CAREGIVER + "')")
public class BookingController {

    private final BookingService bookingService;
    private final UsersService usersService;

    private final UsersRepository usersRepository;
    private final ServiceRepository serviceRepository;
    private final CityRepository cityRepository;

    public BookingController(final BookingService bookingService,
                             UsersService usersService, final UsersRepository usersRepository, final ServiceRepository serviceRepository, CityRepository cityRepository) {
        this.bookingService = bookingService;
        this.usersService = usersService;
        this.usersRepository = usersRepository;
        this.serviceRepository = serviceRepository;
        this.cityRepository = cityRepository;
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
//        return "booking/list";
        return "booking/newBooking";
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

    @PostMapping("/addBooking")
    public String addBooking(@ModelAttribute("booking") @Valid final BookingDTO bookingDTO,
                             final BindingResult bindingResult, final RedirectAttributes redirectAttributes,
                             final Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Data de inicio deve ser no futuro");
            return "redirect:/error";
//            return "error";
//            return "booking/add";
        }

        HttpUserDetails userDetails = (HttpUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Users carerecivier = usersService.findById(userDetails.getId());
        bookingDTO.setCarerecivier(carerecivier.getId());

        Long aLong = bookingService.create(bookingDTO);

        if (aLong == -1) {
            model.addAttribute("errorMessage", "Data de inicio deve ser no futuro");
            return "search/errorFuturo";
        }

        if (aLong == -2) {
            model.addAttribute("errorMessage", "Enfermeiro não disponível neste horário");
            return "search/errorEnfermeiro";
        }


        return "redirect:/bookings/agendamentos";
    }

    @GetMapping("/agendamentos")
    public String getBookings(final Model model) {
        HttpUserDetails userDetails = (HttpUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = usersService.findById(userDetails.getId());
        long userId = user.getId();

        List<Booking> futureBookings = new ArrayList<>();
        List<Booking> pastBookings = new ArrayList<>();
        if (user.getRole() == ROLE.CARERECIVIER) {
            futureBookings = bookingService.findByCarerecivierIdAndStartDateTimeAfter(userId);
            pastBookings = bookingService.findByCarerecivierIdAndEndDateTimeBefore(userId);
        } else if (user.getRole() == ROLE.CAREGIVER) {
            futureBookings = bookingService.findByCaregiverIdAndStartDateTimeAfter(userId);
            pastBookings = bookingService.findByCaregiverIdAndEndDateTimeBefore(userId);
        }

        model.addAttribute("bookings", bookingService.findAll(null, PageRequest.of(0, 10)));
        model.addAttribute("futureBookings", futureBookings);
        model.addAttribute("pastBookings", pastBookings);
        model.addAttribute("role", user.getRole().toString());
        return "booking/schedule";
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
