package edu.pharmacy.service;

import edu.pharmacy.dao.CityRepository;
import edu.pharmacy.exception.BusinessLogicException;
import edu.pharmacy.model.dto.CityFullInfoDTO;
import edu.pharmacy.model.dto.CityToCreateDTO;
import edu.pharmacy.model.dto.CityToUpdateDTO;
import edu.pharmacy.model.entity.City;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CityService extends AbstractService {

    private final CityRepository cityRepository;

    @SneakyThrows
    public CityFullInfoDTO create(String cityToCreateJson) {
        CityToCreateDTO cityToCreateDTO = objectMapper.readValue(cityToCreateJson, CityToCreateDTO.class);
        String name = cityToCreateDTO.getName();
        checkName(name);
        City city = new City();
        city.setName(name);
        City createdCity = cityRepository.save(city);
        return mapFromCityToCityFullInfoDTO(createdCity);
    }

    public List<CityFullInfoDTO> getAll() {
        List<City> cities = cityRepository.findAll();
        return cities.stream().map(this::mapFromCityToCityFullInfoDTO).toList();
    }

    public CityFullInfoDTO get(long id) {
        checkExistence(id);
        City city = cityRepository.getById(id);
        return mapFromCityToCityFullInfoDTO(city);
    }

    @SneakyThrows
    public CityFullInfoDTO update(long id, String updatedCityJson) {
        CityToUpdateDTO cityToUpdateDTO = objectMapper.readValue(updatedCityJson, CityToUpdateDTO.class);
        checkExistence(id);
        City cityToUpdate = cityRepository.getById(id);
        City updatedCity = updateCityFields(cityToUpdate, cityToUpdateDTO);
        return mapFromCityToCityFullInfoDTO(cityRepository.save(updatedCity));
    }

    public long delete(long id) {
        checkExistence(id);
        cityRepository.deleteById(id);
        return id;
    }

    public void checkExistence(long id) {
        if (!cityRepository.existsById(id)) throw new BusinessLogicException("City with this id not found");
    }

    private void checkName(String name) {
        if (cityRepository.existsByName(name)) {
            throw new BusinessLogicException("City with this name is already exists");
        }
        int length = name.length();
        if (length < 3 || length > 50) {
            throw new BusinessLogicException("The maximum length of the city name is 50 characters, and the minimum is 3 characters.");
        }
    }

    private City updateCityFields(City cityToUpdate, CityToUpdateDTO cityToUpdateDTO) {
        String name = cityToUpdateDTO.getName();
        if (name != null) {
            checkName(name);
            cityToUpdate.setName(name);
        }
        return cityToUpdate;
    }

    private CityFullInfoDTO mapFromCityToCityFullInfoDTO(City city) {
        return modelMapper.map(city, CityFullInfoDTO.class);
    }
}
