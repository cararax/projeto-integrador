package com.carara.nursenow.controller;

import com.carara.nursenow.domain.City;
import com.carara.nursenow.model.ROLE;
import com.carara.nursenow.model.RegistrationRequest;
import com.carara.nursenow.repos.CityRepository;
import com.carara.nursenow.service.RegistrationService;
import com.carara.nursenow.util.CustomCollectors;
import com.carara.nursenow.util.WebUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class RegistrationController {
    private final RegistrationService registrationService;
    private final CityRepository cityRepository;

    public RegistrationController(final RegistrationService registrationService, CityRepository cityRepository) {
        this.registrationService = registrationService;
        this.cityRepository = cityRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("roleValues", ROLE.values());
        model.addAttribute("cityValues", cityRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(City::getId, City::getName)));
    }

    @GetMapping("/register/choose-role")
    public String register(@ModelAttribute final RegistrationRequest registrationRequest) {
        return "registration/choose-role";
    }

    @GetMapping("/register/caregiver")
    public String registerCaregiver(@ModelAttribute final RegistrationRequest caregiverRequest) {
        return "registration/caregiver";
    }

    @PostMapping("/register/caregiver")
    public String registerCaregiver(@ModelAttribute @Valid final RegistrationRequest registrationRequest,
                                    final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && registrationService.emailExists(registrationRequest)) {
            bindingResult.rejectValue("email", "registration.register.taken");
        }
        registrationRequest.setRole(ROLE.CAREGIVER);
        if (bindingResult.hasErrors()) {
            return "registration/caregiver";
        }
        registrationService.register(registrationRequest);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("registration.register.success"));
        return "redirect:/login";
    }


    @GetMapping("/register/carerecivier")
    public String registercarerecivier(@ModelAttribute final RegistrationRequest caregiverRequest) {
        return "registration/carerecivier";
    }

    @PostMapping("/register/carerecivier")
    public String registercarerecivier(@ModelAttribute @Valid final RegistrationRequest registrationRequest,
                                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && registrationService.emailExists(registrationRequest)) {
            bindingResult.rejectValue("email", "registration.register.taken");
        }
        registrationRequest.setRole(ROLE.CARERECIVIER);
        if (bindingResult.hasErrors()) {
            log.info("{}", bindingResult.getAllErrors());
            return "registration/carerecivier";
        }
        registrationService.register(registrationRequest);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("registration.register.success"));
        return "redirect:/login";
    }

}

