package com.carara.nursenow.controller;

import com.carara.nursenow.domain.City;
import com.carara.nursenow.domain.Service;
import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.BookingDTO;
import com.carara.nursenow.model.HttpUserDetails;
import com.carara.nursenow.model.ROLE;
import com.carara.nursenow.model.UsersDTO;
import com.carara.nursenow.repos.CityRepository;
import com.carara.nursenow.service.ServiceService;
import com.carara.nursenow.service.UsersService;
import com.carara.nursenow.util.CustomCollectors;
import com.carara.nursenow.util.WebUtils;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/userss")
@PreAuthorize("hasAuthority('" + ROLE.Fields.CAREGIVER + "')")
public class UsersController {

    private final UsersService usersService;
    private final ServiceService serviceService;
    private final CityRepository cityRepository;


    public UsersController(UsersService usersService, ServiceService serviceService, CityRepository cityRepository) {
        this.usersService = usersService;
        this.serviceService = serviceService;
        this.cityRepository = cityRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("roleValues", ROLE.values());
        model.addAttribute("cityValues", cityRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(City::getId, City::getName)));
    }

    @GetMapping("/caregivers")
    public String getCaregiverDetails(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "city", required = false) Long city,
                                      @RequestParam(value = "service", required = false) Long service,
                                      Model model) {

        List<City> allCities = usersService.getAllCities();
        List<Service> allServices = usersService.getAllServicesDistinct();
        List<Users> filteredCaregivers = usersService.findByProperties(name, city, service);

        model.addAttribute("services", allServices);
        model.addAttribute("cities", allCities);
        model.addAttribute("caregivers", filteredCaregivers);
        return "users/caregiverList";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("users") final UsersDTO usersDTO) {
        return "users/add";
    }

    @GetMapping("/profile/{id}")
    public String profile(@PathVariable("id") final Long id, final Model model) {
        Users usersDTO = usersService.findById(id);
        model.addAttribute("users", usersDTO);
        if (usersDTO.getRole() == ROLE.CAREGIVER) {
            return "users/caregiverView";
        } else {
            return "users/carerecivier";
        }
    }
//    @PreAuthorize("hasAuthority('" + ROLE.Fields.CARERECIVIER + "')")

    @GetMapping("/profile/{userId}/service/{serviceId}")
    public String profile(@PathVariable("userId") final Long userId,
                          @PathVariable("serviceId") final Long serviceId,
                          final Model model) {

        HttpUserDetails userDetails = (HttpUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Users caregiver = usersService.findById(userId);
        Users carerecivier = usersService.findById(userDetails.getId());
        Service service = serviceService.findServiceById(serviceId);

        BookingDTO bookingDTO = new BookingDTO();
        model.addAttribute("booking", bookingDTO);
        model.addAttribute("carerecivier", carerecivier);
        model.addAttribute("caregiver", caregiver);
        model.addAttribute("service", service);


        return "booking/add";

    }

    @GetMapping("/profile")
    public String profile(Model model) {
        HttpUserDetails userDetails = (HttpUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = usersService.findById(userDetails.getId());

        model.addAttribute("users", user);

        if (user.getRole() == ROLE.CAREGIVER) {
            return "users/caregiver";
        } else {
            return "users/carerecivier";
        }
    }


    @PostMapping("/add")
    public String add(@ModelAttribute("users") @Valid final UsersDTO usersDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && usersService.emailExists(usersDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.users.email");
        }
        if (bindingResult.hasErrors()) {
            log.info("{}", bindingResult.getAllErrors());
            return "users/add";
        }
        usersService.create(usersDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("users.create.success"));
        return "redirect:/userss";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") final Long id, final Model model) {
        model.addAttribute("users", usersService.get(id));
        return "users/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
                       @ModelAttribute("users") @Valid final UsersDTO usersDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final UsersDTO currentUsersDTO = usersService.get(id);

        if (usersService.emailExists(usersDTO.getEmail()) && !usersDTO.getEmail().equalsIgnoreCase(currentUsersDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.users.email");
        }

        if (bindingResult.hasFieldErrors("email")) {
            bindingResult.rejectValue("email", "Exists.users.email");
        }
        if (bindingResult.hasErrors()) {
            log.info("{}", bindingResult.getAllErrors());
            return "users/edit";
        }
        usersService.update(id, usersDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("users.update.success"));
        return "redirect:/userss/profile";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = usersService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            usersService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("users.delete.success"));
        }
        return "redirect:/userss";
    }

}

