package com.carara.nursenow.service;

import com.carara.nursenow.domain.Experience;
import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.ExperienceDTO;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.repos.ExperienceRepository;
import com.carara.nursenow.repos.UsersRepository;
import com.carara.nursenow.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final UsersRepository usersRepository;

    public ExperienceService(final ExperienceRepository experienceRepository,
            final UsersRepository usersRepository) {
        this.experienceRepository = experienceRepository;
        this.usersRepository = usersRepository;
    }

    public SimplePage<ExperienceDTO> findAll(final String filter, final Pageable pageable) {
        Page<Experience> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = experienceRepository.findAllById(longFilter, pageable);
        } else {
            page = experienceRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(experience -> mapToDTO(experience, new ExperienceDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public ExperienceDTO get(final Long id) {
        return experienceRepository.findById(id)
                .map(experience -> mapToDTO(experience, new ExperienceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ExperienceDTO experienceDTO) {
        final Experience experience = new Experience();
        mapToEntity(experienceDTO, experience);
        return experienceRepository.save(experience).getId();
    }

    public void update(final Long id, final ExperienceDTO experienceDTO) {
        final Experience experience = experienceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(experienceDTO, experience);
        experienceRepository.save(experience);
    }

    public void delete(final Long id) {
        experienceRepository.deleteById(id);
    }

    private ExperienceDTO mapToDTO(final Experience experience, final ExperienceDTO experienceDTO) {
        experienceDTO.setId(experience.getId());
        experienceDTO.setCompany(experience.getCompany());
        experienceDTO.setDescription(experience.getDescription());
        experienceDTO.setStartDate(experience.getStartDate());
        experienceDTO.setEndDate(experience.getEndDate());
        experienceDTO.setCaregiver(experience.getCaregiver() == null ? null : experience.getCaregiver().getId());
        return experienceDTO;
    }

    private Experience mapToEntity(final ExperienceDTO experienceDTO, final Experience experience) {
        experience.setCompany(experienceDTO.getCompany());
        experience.setDescription(experienceDTO.getDescription());
        experience.setStartDate(experienceDTO.getStartDate());
        experience.setEndDate(experienceDTO.getEndDate());
        final Users caregiver = experienceDTO.getCaregiver() == null ? null : usersRepository.findById(experienceDTO.getCaregiver())
                .orElseThrow(() -> new NotFoundException("caregiver not found"));
        experience.setCaregiver(caregiver);
        return experience;
    }

}
