package edu.pharmacy.controller;

import edu.pharmacy.model.dto.CityFullInfoDTO;
import edu.pharmacy.model.dto.PharmacyFullInfoDTO;
import edu.pharmacy.service.CityService;
import edu.pharmacy.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    @PostMapping
    public CityFullInfoDTO create(@RequestBody String cityJson) {
        return cityService.create(cityJson);
    }

    @GetMapping
    public List<CityFullInfoDTO> getAll() {
        return cityService.getAll();
    }

    @GetMapping(value = "{id}")
    public CityFullInfoDTO get(@PathVariable("id") long id) {
        return cityService.get(id);
    }

    @PutMapping(value = "{id}")
    public CityFullInfoDTO update(@PathVariable("id") long id, @RequestBody String updatedCityJson) {
        return cityService.update(id, updatedCityJson);
    }

    @DeleteMapping(value = "{id}")
    public long delete(@PathVariable("id") long id) {
        return cityService.delete(id);
    }
}
