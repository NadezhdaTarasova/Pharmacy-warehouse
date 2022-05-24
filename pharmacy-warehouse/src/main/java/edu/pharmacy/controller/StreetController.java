package edu.pharmacy.controller;

import edu.pharmacy.model.dto.StreetFullInfoDTO;
import edu.pharmacy.service.StreetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/streets")
public class StreetController {

    private final StreetService streetService;

    @PostMapping
    public StreetFullInfoDTO create(@RequestBody String cityStreet) {
        return streetService.create(cityStreet);
    }

    @GetMapping
    public List<StreetFullInfoDTO> getAll() {
        return streetService.getAll();
    }

    @GetMapping(value = "{id}")
    public StreetFullInfoDTO get(@PathVariable("id") long id) {
        return streetService.get(id);
    }

    @PutMapping(value = "{id}")
    public StreetFullInfoDTO update(@PathVariable("id") long id, @RequestBody String updatedStreetJson) {
        return streetService.update(id, updatedStreetJson);
    }

    @DeleteMapping(value = "{id}")
    public long delete(@PathVariable("id") long id) {
        return streetService.delete(id);
    }
}
