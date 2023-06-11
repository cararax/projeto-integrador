package com.carara.nursenow.repos;

import com.carara.nursenow.domain.Experience;
import com.carara.nursenow.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    Page<Experience> findAllById(Long id, Pageable pageable);

    Experience findFirstByCaregiver(Users users);

}
