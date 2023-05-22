package com.carara.my_app.service;

import com.carara.my_app.domain.Booking;
import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.model.ServiceDTO;
import com.carara.my_app.repos.BookingRepository;
import com.carara.my_app.repos.CaregiverRepository;
import com.carara.my_app.repos.ServiceRepository;
import com.carara.my_app.util.NotFoundException;
import com.carara.my_app.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final CaregiverRepository caregiverRepository;
    private final BookingRepository bookingRepository;

    public ServiceService(final ServiceRepository serviceRepository,
            final CaregiverRepository caregiverRepository,
            final BookingRepository bookingRepository) {
        this.serviceRepository = serviceRepository;
        this.caregiverRepository = caregiverRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<ServiceDTO> findAll() {
        final List<com.carara.my_app.domain.Service> services = serviceRepository.findAll(Sort.by("id"));
        return services.stream()
                .map((service) -> mapToDTO(service, new ServiceDTO()))
                .toList();
    }

    public ServiceDTO get(final Long id) {
        return serviceRepository.findById(id)
                .map(service -> mapToDTO(service, new ServiceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ServiceDTO serviceDTO) {
        final com.carara.my_app.domain.Service service = new com.carara.my_app.domain.Service();
        mapToEntity(serviceDTO, service);
        return serviceRepository.save(service).getId();
    }

    public void update(final Long id, final ServiceDTO serviceDTO) {
        final com.carara.my_app.domain.Service service = serviceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(serviceDTO, service);
        serviceRepository.save(service);
    }

    public void delete(final Long id) {
        serviceRepository.deleteById(id);
    }

    private ServiceDTO mapToDTO(final com.carara.my_app.domain.Service service,
            final ServiceDTO serviceDTO) {
        serviceDTO.setId(service.getId());
        serviceDTO.setName(service.getName());
        serviceDTO.setDuration(service.getDuration());
        serviceDTO.setDescription(service.getDescription());
        serviceDTO.setPrice(service.getPrice());
        serviceDTO.setCaregiver(service.getCaregiver() == null ? null : service.getCaregiver().getId());
        return serviceDTO;
    }

    private com.carara.my_app.domain.Service mapToEntity(final ServiceDTO serviceDTO,
            final com.carara.my_app.domain.Service service) {
        service.setName(serviceDTO.getName());
        service.setDuration(serviceDTO.getDuration());
        service.setDescription(serviceDTO.getDescription());
        service.setPrice(serviceDTO.getPrice());
        final Caregiver caregiver = serviceDTO.getCaregiver() == null ? null : caregiverRepository.findById(serviceDTO.getCaregiver())
                .orElseThrow(() -> new NotFoundException("caregiver not found"));
        service.setCaregiver(caregiver);
        return service;
    }

    public String getReferencedWarning(final Long id) {
        final com.carara.my_app.domain.Service service = serviceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Booking servicesBooking = bookingRepository.findFirstByServices(service);
        if (servicesBooking != null) {
            return WebUtils.getMessage("service.booking.services.referenced", servicesBooking.getId());
        }
        return null;
    }

}
