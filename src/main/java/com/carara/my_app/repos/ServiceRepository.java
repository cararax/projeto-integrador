package com.carara.my_app.repos;

import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.domain.Service;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ServiceRepository extends JpaRepository<Service, Long> {

    Service findFirstByCaregiver(Caregiver caregiver);

}
