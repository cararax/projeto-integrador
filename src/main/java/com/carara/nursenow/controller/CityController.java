package com.carara.nursenow.controller;

import com.carara.nursenow.model.CityDTO;
import com.carara.nursenow.model.ROLE;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.service.CityService;
import com.carara.nursenow.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/citys")
@PreAuthorize("hasAuthority('" + ROLE.Fields.CAREGIVER + "')")
public class CityController {

    private final CityService cityService;

    public CityController(final CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
                       @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
                       final Model model) {
        final SimplePage<CityDTO> citys = cityService.findAll(filter, pageable);
        model.addAttribute("citys", citys);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(citys));
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
