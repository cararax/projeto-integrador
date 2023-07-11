package com.carara.nursenow.service;

import com.carara.nursenow.domain.Booking;
import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.ServiceDTO;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.repos.BookingRepository;
import com.carara.nursenow.repos.ServiceRepository;
import com.carara.nursenow.repos.UsersRepository;
import com.carara.nursenow.util.NotFoundException;
import com.carara.nursenow.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final UsersRepository usersRepository;
    private final BookingRepository bookingRepository;

    public ServiceService(final ServiceRepository serviceRepository,
                          final UsersRepository usersRepository, final BookingRepository bookingRepository) {
        this.serviceRepository = serviceRepository;
        this.usersRepository = usersRepository;
        this.bookingRepository = bookingRepository;
    }

    public SimplePage<ServiceDTO> findAll(final String filter, final Pageable pageable) {
        Page<com.carara.nursenow.domain.Service> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = serviceRepository.findAllById(longFilter, pageable);
        } else {
            page = serviceRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(service -> mapToDTO(service, new ServiceDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public ServiceDTO get(final Long id) {
        return serviceRepository.findById(id)
                .map(service -> mapToDTO(service, new ServiceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ServiceDTO serviceDTO, String email) {
        final com.carara.nursenow.domain.Service service = new com.carara.nursenow.domain.Service();
        serviceDTO.setCaregiver(usersRepository.findByEmailIgnoreCase(email).getId());
        mapToEntity(serviceDTO, service);
        return serviceRepository.save(service).getId();
    }

    public Long create(final ServiceDTO serviceDTO) {
        final com.carara.nursenow.domain.Service service = new com.carara.nursenow.domain.Service();
        mapToEntity(serviceDTO, service);
        return serviceRepository.save(service).getId();
    }

    public void update(final Long id, final ServiceDTO serviceDTO) {
        final com.carara.nursenow.domain.Service service = serviceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        serviceDTO.setCaregiver(service.getCaregiver().getId());
        mapToEntity(serviceDTO, service);
        serviceRepository.save(service);
    }

    public void delete(final Long id) {
        serviceRepository.deleteById(id);
    }

    private ServiceDTO mapToDTO(final com.carara.nursenow.domain.Service service,
                                final ServiceDTO serviceDTO) {
        serviceDTO.setId(service.getId());
        serviceDTO.setName(service.getName());
        serviceDTO.setDuration(service.getDuration());
        serviceDTO.setDescription(service.getDescription());
        serviceDTO.setPrice(service.getPrice());
        serviceDTO.setCaregiver(service.getCaregiver() == null ? null : service.getCaregiver().getId());
        return serviceDTO;
    }

    private com.carara.nursenow.domain.Service mapToEntity(final ServiceDTO serviceDTO,
                                                           final com.carara.nursenow.domain.Service service) {
        service.setName(serviceDTO.getName());
        service.setDuration(serviceDTO.getDuration());
        service.setDescription(serviceDTO.getDescription());
        service.setPrice(serviceDTO.getPrice());
        final Users caregiver = serviceDTO.getCaregiver() == null ? null : usersRepository.findById(serviceDTO.getCaregiver())
                .orElseThrow(() -> new NotFoundException("caregiver not found"));
        service.setCaregiver(caregiver);
        return service;
    }

    public String getReferencedWarning(final Long id) {
        final com.carara.nursenow.domain.Service service = serviceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Booking servicesBooking = bookingRepository.findFirstByServices(service);
        if (servicesBooking != null) {
            return WebUtils.getMessage("service.booking.services.referenced", servicesBooking.getId());
        }
        return null;
    }

}
