package com.carara.nursenow.controller;

import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.ROLE;
import com.carara.nursenow.model.ServiceDTO;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.repos.UsersRepository;
import com.carara.nursenow.service.ServiceService;
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
@RequestMapping("/services")
@PreAuthorize("hasAuthority('" + ROLE.Fields.CAREGIVER + "')")
public class ServiceController {

    private final ServiceService serviceService;
    private final UsersRepository usersRepository;

    public ServiceController(final ServiceService serviceService,
                             final UsersRepository usersRepository) {
        this.serviceService = serviceService;
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
        final SimplePage<ServiceDTO> services = serviceService.findAll(filter, pageable);
        model.addAttribute("services", services);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(services));
        return "service/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("service") final ServiceDTO serviceDTO) {
        return "service/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("service") @Valid final ServiceDTO serviceDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "service/add";
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        serviceService.create(serviceDTO, userDetails.getUsername());
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("service.create.success"));
        return "redirect:/userss/profile";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("service", serviceService.get(id));
        return "service/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
                       @ModelAttribute("service") @Valid final ServiceDTO serviceDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "service/edit";
        }
        serviceService.update(id, serviceDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("service.update.success"));
        return "redirect:/userss/profile";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = serviceService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            serviceService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("service.delete.success"));
        }
        return "redirect:/userss/profile";
    }

}
