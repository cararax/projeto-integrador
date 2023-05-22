package com.carara.my_app.controller;

import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.model.ExperienceDTO;
import com.carara.my_app.repos.CaregiverRepository;
import com.carara.my_app.service.ExperienceService;
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
@RequestMapping("/experiences")
public class ExperienceController {

    private final ExperienceService experienceService;
    private final CaregiverRepository caregiverRepository;

    public ExperienceController(final ExperienceService experienceService,
            final CaregiverRepository caregiverRepository) {
        this.experienceService = experienceService;
        this.caregiverRepository = caregiverRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("caregiverValues", caregiverRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Caregiver::getId, Caregiver::getFirstname)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("experiences", experienceService.findAll());
        return "experience/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("experience") final ExperienceDTO experienceDTO) {
        return "experience/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("experience") @Valid final ExperienceDTO experienceDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "experience/add";
        }
        experienceService.create(experienceDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("experience.create.success"));
        return "redirect:/experiences";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("experience", experienceService.get(id));
        return "experience/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("experience") @Valid final ExperienceDTO experienceDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "experience/edit";
        }
        experienceService.update(id, experienceDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("experience.update.success"));
        return "redirect:/experiences";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        experienceService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("experience.delete.success"));
        return "redirect:/experiences";
    }

}
