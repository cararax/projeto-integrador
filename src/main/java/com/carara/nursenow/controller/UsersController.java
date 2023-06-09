package com.carara.nursenow.controller;

import com.carara.nursenow.domain.City;
import com.carara.nursenow.model.ROLE;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.model.UsersDTO;
import com.carara.nursenow.repos.CityRepository;
import com.carara.nursenow.service.UsersService;
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
@RequestMapping("/userss")
@PreAuthorize("hasAuthority('" + ROLE.Fields.CAREGIVER + "')")
public class UsersController {

    private final UsersService usersService;
    private final CityRepository cityRepository;

    public UsersController(final UsersService usersService, final CityRepository cityRepository) {
        this.usersService = usersService;
        this.cityRepository = cityRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("roleValues", ROLE.values());
        model.addAttribute("cityValues", cityRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(City::getId, City::getName)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<UsersDTO> userss = usersService.findAll(filter, pageable);
        model.addAttribute("userss", userss);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(userss));
        return "users/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("users") final UsersDTO usersDTO) {
        return "users/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("users") @Valid final UsersDTO usersDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && usersService.emailExists(usersDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.users.email");
        }
        if (bindingResult.hasErrors()) {
            return "users/add";
        }
        usersService.create(usersDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("users.create.success"));
        return "redirect:/userss";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("users", usersService.get(id));
        return "users/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("users") @Valid final UsersDTO usersDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final UsersDTO currentUsersDTO = usersService.get(id);
        if (!bindingResult.hasFieldErrors("email") &&
                !usersDTO.getEmail().equalsIgnoreCase(currentUsersDTO.getEmail()) &&
                usersService.emailExists(usersDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.users.email");
        }
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }
        usersService.update(id, usersDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("users.update.success"));
        return "redirect:/userss";
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
