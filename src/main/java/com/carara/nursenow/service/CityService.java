package com.carara.nursenow.service;

import com.carara.nursenow.domain.City;
import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.CityDTO;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.repos.CityRepository;
import com.carara.nursenow.repos.UsersRepository;
import com.carara.nursenow.util.NotFoundException;
import com.carara.nursenow.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CityService {

    private final CityRepository cityRepository;
    private final UsersRepository usersRepository;

    public CityService(final CityRepository cityRepository, final UsersRepository usersRepository) {
        this.cityRepository = cityRepository;
        this.usersRepository = usersRepository;
    }

    public SimplePage<CityDTO> findAll(final String filter, final Pageable pageable) {
        Page<City> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = cityRepository.findAllById(longFilter, pageable);
        } else {
            page = cityRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(city -> mapToDTO(city, new CityDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public CityDTO get(final Long id) {
        return cityRepository.findById(id)
                .map(city -> mapToDTO(city, new CityDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CityDTO cityDTO) {
        final City city = new City();
        mapToEntity(cityDTO, city);
        return cityRepository.save(city).getId();
    }

    public void update(final Long id, final CityDTO cityDTO) {
        final City city = cityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(cityDTO, city);
        cityRepository.save(city);
    }

    public void delete(final Long id) {
        cityRepository.deleteById(id);
    }

    private CityDTO mapToDTO(final City city, final CityDTO cityDTO) {
        cityDTO.setId(city.getId());
        cityDTO.setName(city.getName());
        return cityDTO;
    }

    private City mapToEntity(final CityDTO cityDTO, final City city) {
        city.setName(cityDTO.getName());
        return city;
    }

    public boolean nameExists(final String name) {
        return cityRepository.existsByNameIgnoreCase(name);
    }

    public String getReferencedWarning(final Long id) {
        final City city = cityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Users cityUsers = usersRepository.findFirstByCity(city);
        if (cityUsers != null) {
            return WebUtils.getMessage("city.users.city.referenced", cityUsers.getId());
        }
        return null;
    }

}
