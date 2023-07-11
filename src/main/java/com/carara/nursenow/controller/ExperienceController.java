package com.carara.nursenow.controller;

import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.ExperienceDTO;
import com.carara.nursenow.model.ROLE;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.repos.UsersRepository;
import com.carara.nursenow.service.ExperienceService;
import com.carara.nursenow.util.CustomCollectors;
import com.carara.nursenow.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/experiences")
@PreAuthorize("hasAuthority('" + ROLE.Fields.CAREGIVER + "')")
public class ExperienceController {

    private final ExperienceService experienceService;
    private final UsersRepository usersRepository;

    public ExperienceController(final ExperienceService experienceService,
                                final UsersRepository usersRepository) {
        this.experienceService = experienceService;
        this.usersRepository = usersRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("caregiverValues", usersRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Users::getId, Users::getFirstname)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
                       @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
                       final Model model) {
        final SimplePage<ExperienceDTO> experiences = experienceService.findAll(filter, pageable);
        model.addAttribute("experiences", experiences);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(experiences));
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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        experienceService.create(experienceDTO, userDetails.getUsername());
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("experience.create.success"));
        return "redirect:/userss/profile";
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
        return "redirect:/userss/profile";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        experienceService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("experience.delete.success"));
        return "redirect:/userss/profile";
    }

}
