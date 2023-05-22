package com.carara.my_app.repos;

import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.domain.Experience;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    Experience findFirstByCaregiver(Caregiver caregiver);

}
