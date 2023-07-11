package com.carara.nursenow.repos;

import com.carara.nursenow.domain.City;
import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.ROLE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UsersRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {

    Users findByEmailIgnoreCase(String email);

    Page<Users> findAllById(Long id, Pageable pageable);

    boolean existsByEmailIgnoreCase(String email);

    Users findFirstByCity(City city);

    List<Users> findByRole(ROLE role);

//    List<Users> findByNameAndCityAndService(String name, String city, String service);

    List<Users> findByFirstnameAndCity_NameAndService_Name(String firstname, String cityName, String name1);

//    List<Users> findByFirstnameLikeAndLastnameLikeAndCity_NameAndService_Name(
//            String firstname, String lastname, String cityName, String serviceName);


//        @Query("SELECT c FROM Caregiver c WHERE " +
//                "(lower(c.firstname) LIKE lower(concat('%', :firstname,'%')) OR :firstname is null) AND " +
//                "(lower(c.lastname) LIKE lower(concat('%', :lastname,'%')) OR :lastname is null) AND " +
//                "(c.city.name = :city OR :city is null) AND " +
//                "(c.service.name = :service OR :service is null)")
//        List<Users> findByFirstnameLikeAndLastnameLikeAndCity_NameAndService_Name(@Param("firstname") String firstname,
//                                                                                  @Param("lastname") String lastname,
//                                                                                  @Param("city") String city,
//                                                                                  @Param("service") String service);

        //findByFirstnameLikeIgnoreCaseAndLastnameLikeIgnoreCaseAndCity_NameAndService_NameAllIgnoreCase
    @Query("""
            select u from Users u inner join u.service service
            where upper(u.firstname) like upper(?1) and upper(u.lastname) like upper(?2) and upper(u.city.name) = upper(?3) and upper(service.name) = upper(?4)""")
    Optional<Users> findByProperties(String firstname, String lastname, String cityName, String serviceName);
    


}
