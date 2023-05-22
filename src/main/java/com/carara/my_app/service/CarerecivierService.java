package com.carara.my_app.service;

import com.carara.my_app.domain.Booking;
import com.carara.my_app.domain.Carerecivier;
import com.carara.my_app.domain.City;
import com.carara.my_app.model.CarerecivierDTO;
import com.carara.my_app.repos.BookingRepository;
import com.carara.my_app.repos.CarerecivierRepository;
import com.carara.my_app.repos.CityRepository;
import com.carara.my_app.util.NotFoundException;
import com.carara.my_app.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CarerecivierService {

    private final CarerecivierRepository carerecivierRepository;
    private final CityRepository cityRepository;
    private final BookingRepository bookingRepository;

    public CarerecivierService(final CarerecivierRepository carerecivierRepository,
            final CityRepository cityRepository, final BookingRepository bookingRepository) {
        this.carerecivierRepository = carerecivierRepository;
        this.cityRepository = cityRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<CarerecivierDTO> findAll() {
        final List<Carerecivier> carereciviers = carerecivierRepository.findAll(Sort.by("id"));
        return carereciviers.stream()
                .map((carerecivier) -> mapToDTO(carerecivier, new CarerecivierDTO()))
                .toList();
    }

    public CarerecivierDTO get(final Long id) {
        return carerecivierRepository.findById(id)
                .map(carerecivier -> mapToDTO(carerecivier, new CarerecivierDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CarerecivierDTO carerecivierDTO) {
        final Carerecivier carerecivier = new Carerecivier();
        mapToEntity(carerecivierDTO, carerecivier);
        return carerecivierRepository.save(carerecivier).getId();
    }

    public void update(final Long id, final CarerecivierDTO carerecivierDTO) {
        final Carerecivier carerecivier = carerecivierRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(carerecivierDTO, carerecivier);
        carerecivierRepository.save(carerecivier);
    }

    public void delete(final Long id) {
        carerecivierRepository.deleteById(id);
    }

    private CarerecivierDTO mapToDTO(final Carerecivier carerecivier,
            final CarerecivierDTO carerecivierDTO) {
        carerecivierDTO.setId(carerecivier.getId());
        carerecivierDTO.setFirstname(carerecivier.getFirstname());
        carerecivierDTO.setLastname(carerecivier.getLastname());
        carerecivierDTO.setEmail(carerecivier.getEmail());
        carerecivierDTO.setPassword(carerecivier.getPassword());
        carerecivierDTO.setDescription(carerecivier.getDescription());
        carerecivierDTO.setElderyName(carerecivier.getElderyName());
        carerecivierDTO.setElderyBithDate(carerecivier.getElderyBithDate());
        carerecivierDTO.setHealthDetails(carerecivier.getHealthDetails());
        carerecivierDTO.setRole(carerecivier.getRole());
        carerecivierDTO.setCity(carerecivier.getCity() == null ? null : carerecivier.getCity().getId());
        return carerecivierDTO;
    }

    private Carerecivier mapToEntity(final CarerecivierDTO carerecivierDTO,
            final Carerecivier carerecivier) {
        carerecivier.setFirstname(carerecivierDTO.getFirstname());
        carerecivier.setLastname(carerecivierDTO.getLastname());
        carerecivier.setEmail(carerecivierDTO.getEmail());
        carerecivier.setPassword(carerecivierDTO.getPassword());
        carerecivier.setDescription(carerecivierDTO.getDescription());
        carerecivier.setElderyName(carerecivierDTO.getElderyName());
        carerecivier.setElderyBithDate(carerecivierDTO.getElderyBithDate());
        carerecivier.setHealthDetails(carerecivierDTO.getHealthDetails());
        carerecivier.setRole(carerecivierDTO.getRole());
        final City city = carerecivierDTO.getCity() == null ? null : cityRepository.findById(carerecivierDTO.getCity())
                .orElseThrow(() -> new NotFoundException("city not found"));
        carerecivier.setCity(city);
        return carerecivier;
    }

    public boolean emailExists(final String email) {
        return carerecivierRepository.existsByEmailIgnoreCase(email);
    }

    public String getReferencedWarning(final Long id) {
        final Carerecivier carerecivier = carerecivierRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Booking carerecivierBooking = bookingRepository.findFirstByCarerecivier(carerecivier);
        if (carerecivierBooking != null) {
            return WebUtils.getMessage("carerecivier.booking.carerecivier.referenced", carerecivierBooking.getId());
        }
        return null;
    }

}
