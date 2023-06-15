package com.carara.nursenow.service;

import com.carara.nursenow.domain.Booking;
import com.carara.nursenow.domain.City;
import com.carara.nursenow.domain.Experience;
import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.model.UsersDTO;
import com.carara.nursenow.repos.BookingRepository;
import com.carara.nursenow.repos.CityRepository;
import com.carara.nursenow.repos.ExperienceRepository;
import com.carara.nursenow.repos.ServiceRepository;
import com.carara.nursenow.repos.UsersRepository;
import com.carara.nursenow.util.NotFoundException;
import com.carara.nursenow.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExperienceRepository experienceRepository;
    private final ServiceRepository serviceRepository;
    private final BookingRepository bookingRepository;

    public UsersService(final UsersRepository usersRepository, final CityRepository cityRepository,
                        final PasswordEncoder passwordEncoder, final ExperienceRepository experienceRepository,
                        final ServiceRepository serviceRepository, final BookingRepository bookingRepository) {
        this.usersRepository = usersRepository;
        this.cityRepository = cityRepository;
        this.passwordEncoder = passwordEncoder;
        this.experienceRepository = experienceRepository;
        this.serviceRepository = serviceRepository;
        this.bookingRepository = bookingRepository;
    }

    public SimplePage<UsersDTO> findAll(final String filter, final Pageable pageable) {
        Page<Users> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = usersRepository.findAllById(longFilter, pageable);
        } else {
            page = usersRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(users -> mapToDTO(users, new UsersDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public UsersDTO get(final Long id) {
        return usersRepository.findById(id)
                .map(users -> mapToDTO(users, new UsersDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Users getByEmailIgnoreCase(String username) {
        return usersRepository.findByEmailIgnoreCase(username);
    }

    public Long create(final UsersDTO usersDTO) {
        final Users users = new Users();
        mapToEntity(usersDTO, users);
        return usersRepository.save(users).getId();
    }

    public void update(final Long id, final UsersDTO usersDTO) {
//        final Users users = usersRepository.findById(id)
//                .orElseThrow(NotFoundException::new);
//        mapToEntity(usersDTO, users);
//        usersRepository.save(users);

        UsersDTO existingUser = getUserById(id);

        if (existingUser != null) {
            if (usersDTO.getFirstname() != null) {
                existingUser.setFirstname(usersDTO.getFirstname());
            }

            if (usersDTO.getLastname() != null) {
                existingUser.setLastname(usersDTO.getLastname());
            }

            if (usersDTO.getEmail() != null) {
                existingUser.setEmail(usersDTO.getEmail());
            }

            if (usersDTO.getPassword() != null) {
                existingUser.setPassword(usersDTO.getPassword());
            }

//            if (usersDTO.getRole() = null) {
//                existingUser.setRole(usersDTO.getRole());
//            }

            if (usersDTO.getDescription() != null) {
                existingUser.setDescription(usersDTO.getDescription());
            }

            if (usersDTO.getElderyName() != null) {
                existingUser.setElderyName(usersDTO.getElderyName());
            }

            if (usersDTO.getHealthDetails() != null) {
                existingUser.setHealthDetails(usersDTO.getHealthDetails());
            }

            if (usersDTO.getElderyBirthDate() != null) {
                existingUser.setElderyBirthDate(usersDTO.getElderyBirthDate());
            }

            if (usersDTO.getCity() != null) {
//                cityRepository.findById(usersDTO.getCity()).ifPresent(existingUser::setCity);
                existingUser.setCity(usersDTO.getCity());
            }
            Users user = new Users();
            mapToEntity(usersDTO, user);
            usersRepository.save(user);

        }
    }

    public void delete(final Long id) {
        usersRepository.deleteById(id);
    }

    private UsersDTO mapToDTO(final Users users, final UsersDTO usersDTO) {
        usersDTO.setId(users.getId());
        usersDTO.setFirstname(users.getFirstname());
        usersDTO.setLastname(users.getLastname());
        usersDTO.setEmail(users.getEmail());
        usersDTO.setPassword(passwordEncoder.encode(users.getPassword()));
        usersDTO.setRole(users.getRole());
        usersDTO.setDescription(users.getDescription());
        usersDTO.setElderyName(users.getElderyName());
        usersDTO.setHealthDetails(users.getHealthDetails());
        usersDTO.setElderyBirthDate(users.getElderyBirthDate());
        usersDTO.setCity(users.getCity() == null ? null : users.getCity().getId());
        return usersDTO;
    }

    private Users mapToEntity(final UsersDTO usersDTO, final Users users) {
        users.setFirstname(usersDTO.getFirstname());
        users.setLastname(usersDTO.getLastname());
        users.setEmail(usersDTO.getEmail());
        users.setPassword(usersDTO.getPassword());
        users.setRole(usersDTO.getRole());
        users.setDescription(usersDTO.getDescription());
        users.setElderyName(usersDTO.getElderyName());
        users.setHealthDetails(usersDTO.getHealthDetails());
        users.setElderyBirthDate(usersDTO.getElderyBirthDate());
        final City city = usersDTO.getCity() == null ? null : cityRepository.findById(usersDTO.getCity())
                .orElseThrow(() -> new NotFoundException("city not found"));
        users.setCity(city);
        return users;
    }

    public boolean emailExists(final String email) {
        return usersRepository.existsByEmailIgnoreCase(email);
    }

    public String getReferencedWarning(final Long id) {
        final Users users = usersRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Experience caregiverExperience = experienceRepository.findFirstByCaregiver(users);
        if (caregiverExperience != null) {
            return WebUtils.getMessage("users.experience.caregiver.referenced", caregiverExperience.getId());
        }
        final com.carara.nursenow.domain.Service caregiverService = serviceRepository.findFirstByCaregiver(users);
        if (caregiverService != null) {
            return WebUtils.getMessage("users.service.caregiver.referenced", caregiverService.getId());
        }
        final Booking caregiverBooking = bookingRepository.findFirstByCaregiver(users);
        if (caregiverBooking != null) {
            return WebUtils.getMessage("users.booking.caregiver.referenced", caregiverBooking.getId());
        }
        final Booking carerecivierBooking = bookingRepository.findFirstByCarerecivier(users);
        if (carerecivierBooking != null) {
            return WebUtils.getMessage("users.booking.carerecivier.referenced", carerecivierBooking.getId());
        }
        return null;
    }

    public void updateUser(Long id, UsersDTO usersDTO) {
        UsersDTO existingUser = getUserById(id);

        if (existingUser != null) {
            if (usersDTO.getFirstname() != null) {
                existingUser.setFirstname(usersDTO.getFirstname());
            }

            if (usersDTO.getLastname() != null) {
                existingUser.setLastname(usersDTO.getLastname());
            }

            if (usersDTO.getEmail() != null) {
                existingUser.setEmail(usersDTO.getEmail());
            }

            if (usersDTO.getPassword() != null) {
                existingUser.setPassword(usersDTO.getPassword());
            }

//            if (usersDTO.getRole() = null) {
//                existingUser.setRole(usersDTO.getRole());
//            }

            if (usersDTO.getDescription() != null) {
                existingUser.setDescription(usersDTO.getDescription());
            }

            if (usersDTO.getElderyName() != null) {
                existingUser.setElderyName(usersDTO.getElderyName());
            }

            if (usersDTO.getHealthDetails() != null) {
                existingUser.setHealthDetails(usersDTO.getHealthDetails());
            }

            if (usersDTO.getElderyBirthDate() != null) {
                existingUser.setElderyBirthDate(usersDTO.getElderyBirthDate());
            }

            if (usersDTO.getCity() != null) {
//                cityRepository.findById(usersDTO.getCity()).ifPresent(existingUser::setCity);
                existingUser.setCity(usersDTO.getCity());
            }

            update(id, existingUser);
        }
    }

    public UsersDTO getUserById(Long id) {
        return usersRepository.findById(id)
                .map(users -> mapToDTO(users, new UsersDTO()))
                .orElseThrow(NotFoundException::new);
    }
}
