package com.carara.my_app.service;

import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.domain.Carerecivier;
import com.carara.my_app.domain.City;
import com.carara.my_app.model.CityDTO;
import com.carara.my_app.repos.CaregiverRepository;
import com.carara.my_app.repos.CarerecivierRepository;
import com.carara.my_app.repos.CityRepository;
import com.carara.my_app.util.NotFoundException;
import com.carara.my_app.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CityService {

    private final CityRepository cityRepository;
    private final CaregiverRepository caregiverRepository;
    private final CarerecivierRepository carerecivierRepository;

    public CityService(final CityRepository cityRepository,
            final CaregiverRepository caregiverRepository,
            final CarerecivierRepository carerecivierRepository) {
        this.cityRepository = cityRepository;
        this.caregiverRepository = caregiverRepository;
        this.carerecivierRepository = carerecivierRepository;
    }

    public List<CityDTO> findAll() {
        final List<City> citys = cityRepository.findAll(Sort.by("id"));
        return citys.stream()
                .map((city) -> mapToDTO(city, new CityDTO()))
                .toList();
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
        final Caregiver cityCaregiver = caregiverRepository.findFirstByCity(city);
        if (cityCaregiver != null) {
            return WebUtils.getMessage("city.caregiver.city.referenced", cityCaregiver.getId());
        }
        final Carerecivier cityCarerecivier = carerecivierRepository.findFirstByCity(city);
        if (cityCarerecivier != null) {
            return WebUtils.getMessage("city.carerecivier.city.referenced", cityCarerecivier.getId());
        }
        return null;
    }

}
