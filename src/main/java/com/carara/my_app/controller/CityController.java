package com.carara.my_app.controller;

import com.carara.my_app.model.CityDTO;
import com.carara.my_app.service.CityService;
import com.carara.my_app.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/citys")
public class CityController {

    private final CityService cityService;

    public CityController(final CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("citys", cityService.findAll());
        return "city/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("city") final CityDTO cityDTO) {
        return "city/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("city") @Valid final CityDTO cityDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") && cityService.nameExists(cityDTO.getName())) {
            bindingResult.rejectValue("name", "Exists.city.name");
        }
        if (bindingResult.hasErrors()) {
            return "city/add";
        }
        cityService.create(cityDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("city.create.success"));
        return "redirect:/citys";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("city", cityService.get(id));
        return "city/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("city") @Valid final CityDTO cityDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        final CityDTO currentCityDTO = cityService.get(id);
        if (!bindingResult.hasFieldErrors("name") &&
                !cityDTO.getName().equalsIgnoreCase(currentCityDTO.getName()) &&
                cityService.nameExists(cityDTO.getName())) {
            bindingResult.rejectValue("name", "Exists.city.name");
        }
        if (bindingResult.hasErrors()) {
            return "city/edit";
        }
        cityService.update(id, cityDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("city.update.success"));
        return "redirect:/citys";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = cityService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            cityService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("city.delete.success"));
        }
        return "redirect:/citys";
    }

}
