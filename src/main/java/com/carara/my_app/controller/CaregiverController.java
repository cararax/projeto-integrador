package com.carara.my_app.controller;

import com.carara.my_app.domain.City;
import com.carara.my_app.model.CaregiverDTO;
import com.carara.my_app.model.Role;
import com.carara.my_app.repos.CityRepository;
import com.carara.my_app.service.CaregiverService;
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
@RequestMapping("/caregivers")
public class CaregiverController {

    private final CaregiverService caregiverService;
    private final CityRepository cityRepository;

    public CaregiverController(final CaregiverService caregiverService,
            final CityRepository cityRepository) {
        this.caregiverService = caregiverService;
        this.cityRepository = cityRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("roleValues", Role.values());
        model.addAttribute("cityValues", cityRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(City::getId, City::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("caregivers", caregiverService.findAll());
        return "caregiver/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("caregiver") final CaregiverDTO caregiverDTO) {
        return "caregiver/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("caregiver") @Valid final CaregiverDTO caregiverDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && caregiverService.emailExists(caregiverDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.caregiver.email");
        }
        if (bindingResult.hasErrors()) {
            return "caregiver/add";
        }
        caregiverService.create(caregiverDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("caregiver.create.success"));
        return "redirect:/caregivers";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("caregiver", caregiverService.get(id));
        return "caregiver/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("caregiver") @Valid final CaregiverDTO caregiverDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final CaregiverDTO currentCaregiverDTO = caregiverService.get(id);
        if (!bindingResult.hasFieldErrors("email") &&
                !caregiverDTO.getEmail().equalsIgnoreCase(currentCaregiverDTO.getEmail()) &&
                caregiverService.emailExists(caregiverDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.caregiver.email");
        }
        if (bindingResult.hasErrors()) {
            return "caregiver/edit";
        }
        caregiverService.update(id, caregiverDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("caregiver.update.success"));
        return "redirect:/caregivers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = caregiverService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            caregiverService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("caregiver.delete.success"));
        }
        return "redirect:/caregivers";
    }

}
