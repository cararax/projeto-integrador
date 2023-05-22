package com.carara.my_app.repos;

import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.domain.Carerecivier;
import com.carara.my_app.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CarerecivierRepository extends JpaRepository<Carerecivier, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Carerecivier findFirstByCity(City city);

    Optional<Carerecivier> findByEmail(String email);


}
