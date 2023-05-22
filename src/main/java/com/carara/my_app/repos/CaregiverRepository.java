package com.carara.my_app.repos;

import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Caregiver findFirstByCity(City city);

    Optional<Caregiver> findByEmail(String email);

}
