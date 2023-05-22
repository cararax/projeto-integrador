package com.carara.my_app.controller;

import com.carara.my_app.domain.City;
import com.carara.my_app.model.CarerecivierDTO;
import com.carara.my_app.model.Role;
import com.carara.my_app.repos.CityRepository;
import com.carara.my_app.service.CarerecivierService;
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
@RequestMapping("/carereciviers")
public class CarerecivierController {

    private final CarerecivierService carerecivierService;
    private final CityRepository cityRepository;

    public CarerecivierController(final CarerecivierService carerecivierService,
            final CityRepository cityRepository) {
        this.carerecivierService = carerecivierService;
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
        model.addAttribute("carereciviers", carerecivierService.findAll());
        return "carerecivier/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("carerecivier") final CarerecivierDTO carerecivierDTO) {
        return "carerecivier/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("carerecivier") @Valid final CarerecivierDTO carerecivierDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && carerecivierService.emailExists(carerecivierDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.carerecivier.email");
        }
        if (bindingResult.hasErrors()) {
            return "carerecivier/add";
        }
        carerecivierService.create(carerecivierDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("carerecivier.create.success"));
        return "redirect:/carereciviers";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("carerecivier", carerecivierService.get(id));
        return "carerecivier/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("carerecivier") @Valid final CarerecivierDTO carerecivierDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final CarerecivierDTO currentCarerecivierDTO = carerecivierService.get(id);
        if (!bindingResult.hasFieldErrors("email") &&
                !carerecivierDTO.getEmail().equalsIgnoreCase(currentCarerecivierDTO.getEmail()) &&
                carerecivierService.emailExists(carerecivierDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.carerecivier.email");
        }
        if (bindingResult.hasErrors()) {
            return "carerecivier/edit";
        }
        carerecivierService.update(id, carerecivierDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("carerecivier.update.success"));
        return "redirect:/carereciviers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = carerecivierService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            carerecivierService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("carerecivier.delete.success"));
        }
        return "redirect:/carereciviers";
    }

}
