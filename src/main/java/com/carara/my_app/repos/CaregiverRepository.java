package com.carara.my_app.repos;

import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Caregiver findFirstByCity(City city);

}
