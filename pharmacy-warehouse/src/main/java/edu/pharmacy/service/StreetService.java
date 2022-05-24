package edu.pharmacy.service;

import edu.pharmacy.dao.CityRepository;
import edu.pharmacy.dao.StreetRepository;
import edu.pharmacy.exception.BusinessLogicException;
import edu.pharmacy.model.dto.StreetFullInfoDTO;
import edu.pharmacy.model.dto.StreetToCreateDTO;
import edu.pharmacy.model.dto.StreetToUpdateDTO;
import edu.pharmacy.model.entity.City;
import edu.pharmacy.model.entity.Street;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StreetService extends AbstractService {

    private final StreetRepository streetRepository;
    private final CityRepository cityRepository;
    private final CityService cityService;

    @SneakyThrows
    public StreetFullInfoDTO create(String streetJson) {
        StreetToCreateDTO streetToCreateDTO = objectMapper.readValue(streetJson, StreetToCreateDTO.class);
        long cityId = streetToCreateDTO.getCityId();
        cityService.checkExistence(cityId);
        City city = cityRepository.getById(cityId);
        String streetName = streetToCreateDTO.getName();
        checkName(streetName);
        Street street = new Street();
        street.setCity(city);
        street.setName(streetName);
        Street createdStreet = streetRepository.save(street);
        return mapFromStreetToStreetFullInfoDTO(createdStreet);
    }

    public List<StreetFullInfoDTO> getAll() {
        List<Street> streets = streetRepository.findAll();
        return streets.stream().map(this::mapFromStreetToStreetFullInfoDTO).toList();
    }

    public StreetFullInfoDTO get(long id) {
        checkExistence(id);
        Street street = streetRepository.getById(id);
        return mapFromStreetToStreetFullInfoDTO(street);
    }

    @SneakyThrows
    public StreetFullInfoDTO update(long id, String updatedStreetJson) {
        StreetToUpdateDTO streetToUpdateDTO = objectMapper.readValue(updatedStreetJson, StreetToUpdateDTO.class);
        checkExistence(id);
        Street streetToUpdate = streetRepository.getById(id);
        Street updatedStreet = updateStreetFields(streetToUpdate, streetToUpdateDTO);
        return mapFromStreetToStreetFullInfoDTO(streetRepository.save(updatedStreet));
    }

    public long delete(long id) {
        checkExistence(id);
        streetRepository.deleteById(id);
        return id;
    }

    public void checkExistence(long id) {
        if (!streetRepository.existsById(id)) throw new BusinessLogicException("Street with this id not found");
    }

    private void checkName(String name) {
        if (streetRepository.existsByName(name)) {
            throw new BusinessLogicException("Street with this name is already exists");
        }
        int length = name.length();
        if (length < 3 || length > 50) {
            throw new BusinessLogicException("The maximum length of the street name is 50 characters, and the minimum is 3 characters.");
        }
    }

    private Street updateStreetFields(Street street, StreetToUpdateDTO streetToUpdateDTO) {
        street.setName(streetToUpdateDTO.getName());
        return street;
    }

    private StreetFullInfoDTO mapFromStreetToStreetFullInfoDTO(Street street) {
        StreetFullInfoDTO streetFullInfoDTO = modelMapper.map(street, StreetFullInfoDTO.class);
        streetFullInfoDTO.setCityName(street.getCity().getName());
        return streetFullInfoDTO;
    }
}
