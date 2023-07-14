package com.carara.nursenow.service;

import com.carara.nursenow.domain.Booking;
import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.BookingDTO;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.repos.BookingRepository;
import com.carara.nursenow.repos.ServiceRepository;
import com.carara.nursenow.repos.UsersRepository;
import com.carara.nursenow.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UsersRepository usersRepository;
    private final ServiceRepository serviceRepository;

    public BookingService(final BookingRepository bookingRepository,
                          final UsersRepository usersRepository, final ServiceRepository serviceRepository) {
        this.bookingRepository = bookingRepository;
        this.usersRepository = usersRepository;
        this.serviceRepository = serviceRepository;
    }

    public SimplePage<BookingDTO> findAll(final String filter, final Pageable pageable) {
        Page<Booking> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = bookingRepository.findAllById(longFilter, pageable);
        } else {
            page = bookingRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(booking -> mapToDTO(booking, new BookingDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public BookingDTO get(final Long id) {
        return bookingRepository.findById(id)
                .map(booking -> mapToDTO(booking, new BookingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BookingDTO bookingDTO) {
        Optional<com.carara.nursenow.domain.Service> service =
                serviceRepository.findById(bookingDTO.getServices());

        Duration duration = Duration.ofMinutes(service.get().getDuration().longValue());
        LocalDateTime endDateTime = bookingDTO.getStartDateTime().plus(duration);
        bookingDTO.setEndDateTime(endDateTime);

        //todo: check if caregiver is available

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
        final Users caregiver = bookingDTO.getCaregiver() == null ? null : usersRepository.findById(bookingDTO.getCaregiver())
                .orElseThrow(() -> new NotFoundException("caregiver not found"));
        booking.setCaregiver(caregiver);
        final Users carerecivier = bookingDTO.getCarerecivier() == null ? null : usersRepository.findById(bookingDTO.getCarerecivier())
                .orElseThrow(() -> new NotFoundException("carerecivier not found"));
        booking.setCarerecivier(carerecivier);
        final com.carara.nursenow.domain.Service services = bookingDTO.getServices() == null ? null : serviceRepository.findById(bookingDTO.getServices())
                .orElseThrow(() -> new NotFoundException("services not found"));
        booking.setServices(services);
        return booking;
    }

}
