package com.carara.nursenow.repos;

import com.carara.nursenow.domain.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CityRepository extends JpaRepository<City, Long> {

    Page<City> findAllById(Long id, Pageable pageable);

    boolean existsByNameIgnoreCase(String name);

}
