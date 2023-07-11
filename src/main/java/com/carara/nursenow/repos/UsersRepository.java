package com.carara.nursenow.repos;

import com.carara.nursenow.domain.City;
import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.ROLE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface UsersRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {

    Users findByEmailIgnoreCase(String email);

    Page<Users> findAllById(Long id, Pageable pageable);

    boolean existsByEmailIgnoreCase(String email);

    Users findFirstByCity(City city);

    List<Users> findByRole(ROLE role);

}
