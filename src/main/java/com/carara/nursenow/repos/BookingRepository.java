package com.carara.nursenow.repos;

import com.carara.nursenow.domain.Booking;
import com.carara.nursenow.domain.Service;
import com.carara.nursenow.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllById(Long id, Pageable pageable);

    Booking findFirstByCaregiver(Users users);

    Booking findFirstByCarerecivier(Users users);

    Booking findFirstByServices(Service service);

    List<Booking> findByCaregiverIdAndStartDateTimeBeforeAndEndDateTimeAfter(
            Long caregiverId, LocalDateTime endDateTime, LocalDateTime startDateTime);

    List<Booking> findByCarerecivierIdAndStartDateTimeAfter(Long caregiverId, LocalDateTime startDateTime);

    List<Booking> findByCarerecivierIdAndEndDateTimeBefore(Long carerecivierId, LocalDateTime endDateTime);

    List<Booking> findByCaregiverIdAndStartDateTimeAfter(long userId, LocalDateTime currentDateTime);

    List<Booking> findByCaregiverIdAndEndDateTimeBefore(long userId, LocalDateTime currentDateTime);
}
