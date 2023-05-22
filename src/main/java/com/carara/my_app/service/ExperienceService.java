package com.carara.my_app.service;

import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.domain.Experience;
import com.carara.my_app.model.ExperienceDTO;
import com.carara.my_app.repos.CaregiverRepository;
import com.carara.my_app.repos.ExperienceRepository;
import com.carara.my_app.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final CaregiverRepository caregiverRepository;

    public ExperienceService(final ExperienceRepository experienceRepository,
            final CaregiverRepository caregiverRepository) {
        this.experienceRepository = experienceRepository;
        this.caregiverRepository = caregiverRepository;
    }

    public List<ExperienceDTO> findAll() {
        final List<Experience> experiences = experienceRepository.findAll(Sort.by("id"));
        return experiences.stream()
                .map((experience) -> mapToDTO(experience, new ExperienceDTO()))
                .toList();
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
        final Caregiver caregiver = experienceDTO.getCaregiver() == null ? null : caregiverRepository.findById(experienceDTO.getCaregiver())
                .orElseThrow(() -> new NotFoundException("caregiver not found"));
        experience.setCaregiver(caregiver);
        return experience;
    }

}
