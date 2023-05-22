package com.carara.my_app.service;

import com.carara.my_app.domain.Booking;
import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.domain.City;
import com.carara.my_app.domain.Experience;
import com.carara.my_app.model.CaregiverDTO;
import com.carara.my_app.repos.BookingRepository;
import com.carara.my_app.repos.CaregiverRepository;
import com.carara.my_app.repos.CityRepository;
import com.carara.my_app.repos.ExperienceRepository;
import com.carara.my_app.repos.ServiceRepository;
import com.carara.my_app.util.NotFoundException;
import com.carara.my_app.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CaregiverService {

    private final CaregiverRepository caregiverRepository;
    private final CityRepository cityRepository;
    private final ExperienceRepository experienceRepository;
    private final ServiceRepository serviceRepository;
    private final BookingRepository bookingRepository;

    public CaregiverService(final CaregiverRepository caregiverRepository,
            final CityRepository cityRepository, final ExperienceRepository experienceRepository,
            final ServiceRepository serviceRepository, final BookingRepository bookingRepository) {
        this.caregiverRepository = caregiverRepository;
        this.cityRepository = cityRepository;
        this.experienceRepository = experienceRepository;
        this.serviceRepository = serviceRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<CaregiverDTO> findAll() {
        final List<Caregiver> caregivers = caregiverRepository.findAll(Sort.by("id"));
        return caregivers.stream()
                .map((caregiver) -> mapToDTO(caregiver, new CaregiverDTO()))
                .toList();
    }

    public CaregiverDTO get(final Long id) {
        return caregiverRepository.findById(id)
                .map(caregiver -> mapToDTO(caregiver, new CaregiverDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CaregiverDTO caregiverDTO) {
        final Caregiver caregiver = new Caregiver();
        mapToEntity(caregiverDTO, caregiver);
        return caregiverRepository.save(caregiver).getId();
    }

    public void update(final Long id, final CaregiverDTO caregiverDTO) {
        final Caregiver caregiver = caregiverRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(caregiverDTO, caregiver);
        caregiverRepository.save(caregiver);
    }

    public void delete(final Long id) {
        caregiverRepository.deleteById(id);
    }

    private CaregiverDTO mapToDTO(final Caregiver caregiver, final CaregiverDTO caregiverDTO) {
        caregiverDTO.setId(caregiver.getId());
        caregiverDTO.setFirstname(caregiver.getFirstname());
        caregiverDTO.setLastname(caregiver.getLastname());
        caregiverDTO.setEmail(caregiver.getEmail());
        caregiverDTO.setPassword(caregiver.getPassword());
        caregiverDTO.setDescription(caregiver.getDescription());
        caregiverDTO.setRole(caregiver.getRole());
        caregiverDTO.setCity(caregiver.getCity() == null ? null : caregiver.getCity().getId());
        return caregiverDTO;
    }

    private Caregiver mapToEntity(final CaregiverDTO caregiverDTO, final Caregiver caregiver) {
        caregiver.setFirstname(caregiverDTO.getFirstname());
        caregiver.setLastname(caregiverDTO.getLastname());
        caregiver.setEmail(caregiverDTO.getEmail());
        caregiver.setPassword(caregiverDTO.getPassword());
        caregiver.setDescription(caregiverDTO.getDescription());
        caregiver.setRole(caregiverDTO.getRole());
        final City city = caregiverDTO.getCity() == null ? null : cityRepository.findById(caregiverDTO.getCity())
                .orElseThrow(() -> new NotFoundException("city not found"));
        caregiver.setCity(city);
        return caregiver;
    }

    public boolean emailExists(final String email) {
        return caregiverRepository.existsByEmailIgnoreCase(email);
    }

    public String getReferencedWarning(final Long id) {
        final Caregiver caregiver = caregiverRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Experience caregiverExperience = experienceRepository.findFirstByCaregiver(caregiver);
        if (caregiverExperience != null) {
            return WebUtils.getMessage("caregiver.experience.caregiver.referenced", caregiverExperience.getId());
        }
        final com.carara.my_app.domain.Service caregiverService = serviceRepository.findFirstByCaregiver(caregiver);
        if (caregiverService != null) {
            return WebUtils.getMessage("caregiver.service.caregiver.referenced", caregiverService.getId());
        }
        final Booking caregiverBooking = bookingRepository.findFirstByCaregiver(caregiver);
        if (caregiverBooking != null) {
            return WebUtils.getMessage("caregiver.booking.caregiver.referenced", caregiverBooking.getId());
        }
        return null;
    }

}
