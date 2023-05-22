package com.carara.my_app.repos;

import com.carara.my_app.domain.Booking;
import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.domain.Carerecivier;
import com.carara.my_app.domain.Service;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findFirstByCaregiver(Caregiver caregiver);

    Booking findFirstByCarerecivier(Carerecivier carerecivier);

    Booking findFirstByServices(Service service);

}
