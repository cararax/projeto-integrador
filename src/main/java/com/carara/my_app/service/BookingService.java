package com.carara.my_app.service;

import com.carara.my_app.domain.Booking;
import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.domain.Carerecivier;
import com.carara.my_app.model.BookingDTO;
import com.carara.my_app.repos.BookingRepository;
import com.carara.my_app.repos.CaregiverRepository;
import com.carara.my_app.repos.CarerecivierRepository;
import com.carara.my_app.repos.ServiceRepository;
import com.carara.my_app.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CaregiverRepository caregiverRepository;
    private final CarerecivierRepository carerecivierRepository;
    private final ServiceRepository serviceRepository;

    public BookingService(final BookingRepository bookingRepository,
            final CaregiverRepository caregiverRepository,
            final CarerecivierRepository carerecivierRepository,
            final ServiceRepository serviceRepository) {
        this.bookingRepository = bookingRepository;
        this.caregiverRepository = caregiverRepository;
        this.carerecivierRepository = carerecivierRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<BookingDTO> findAll() {
        final List<Booking> bookings = bookingRepository.findAll(Sort.by("id"));
        return bookings.stream()
                .map((booking) -> mapToDTO(booking, new BookingDTO()))
                .toList();
    }

    public BookingDTO get(final Long id) {
        return bookingRepository.findById(id)
                .map(booking -> mapToDTO(booking, new BookingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BookingDTO bookingDTO) {
        final Booking booking = new Booking();
        mapToEntity(bookingDTO, booking);
        return bookingRepository.save(booking).getId();
    }

    public void update(final Long id, final BookingDTO bookingDTO) {
        final Booking booking = bookingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bookingDTO, booking);
        bookingRepository.save(booking);
    }

    public void delete(final Long id) {
        bookingRepository.deleteById(id);
    }

    private BookingDTO mapToDTO(final Booking booking, final BookingDTO bookingDTO) {
        bookingDTO.setId(booking.getId());
        bookingDTO.setStartDateTime(booking.getStartDateTime());
        bookingDTO.setEndDateTime(booking.getEndDateTime());
        bookingDTO.setCaregiver(booking.getCaregiver() == null ? null : booking.getCaregiver().getId());
        bookingDTO.setCarerecivier(booking.getCarerecivier() == null ? null : booking.getCarerecivier().getId());
        bookingDTO.setServices(booking.getServices() == null ? null : booking.getServices().getId());
        return bookingDTO;
    }

    private Booking mapToEntity(final BookingDTO bookingDTO, final Booking booking) {
        booking.setStartDateTime(bookingDTO.getStartDateTime());
        booking.setEndDateTime(bookingDTO.getEndDateTime());
        final Caregiver caregiver = bookingDTO.getCaregiver() == null ? null : caregiverRepository.findById(bookingDTO.getCaregiver())
                .orElseThrow(() -> new NotFoundException("caregiver not found"));
        booking.setCaregiver(caregiver);
        final Carerecivier carerecivier = bookingDTO.getCarerecivier() == null ? null : carerecivierRepository.findById(bookingDTO.getCarerecivier())
                .orElseThrow(() -> new NotFoundException("carerecivier not found"));
        booking.setCarerecivier(carerecivier);
        final com.carara.my_app.domain.Service services = bookingDTO.getServices() == null ? null : serviceRepository.findById(bookingDTO.getServices())
                .orElseThrow(() -> new NotFoundException("services not found"));
        booking.setServices(services);
        return booking;
    }

}
