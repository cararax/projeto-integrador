package com.carara.nursenow.repos;

import com.carara.nursenow.domain.Service;
import com.carara.nursenow.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ServiceRepository extends JpaRepository<Service, Long> {

    Page<Service> findAllById(Long id, Pageable pageable);

    Service findFirstByCaregiver(Users users);

    List<Service> findDistinctByName(String name);

}
