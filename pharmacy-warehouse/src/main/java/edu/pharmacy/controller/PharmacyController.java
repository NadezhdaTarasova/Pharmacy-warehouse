package edu.pharmacy.controller;

import edu.pharmacy.model.dto.PharmacyFullInfoDTO;
import edu.pharmacy.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pharmacies")
@Log4j2
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @PostMapping
    public PharmacyFullInfoDTO create(@RequestBody String accountJson) {
        return pharmacyService.create(accountJson);
    }

    @GetMapping
    public List<PharmacyFullInfoDTO> getAll() {
        return pharmacyService.getAll();
    }

    @GetMapping(value = "{id}")
    public PharmacyFullInfoDTO get(@PathVariable("id") long id) {
        return pharmacyService.get(id);
    }

    @PutMapping(value = "{id}")
    public PharmacyFullInfoDTO update(@PathVariable("id") long id, @RequestBody String updatedAccountJson) {
       return pharmacyService.update(id, updatedAccountJson);
    }

    @DeleteMapping(value = "{id}")
    public long delete(@PathVariable("id") long id) {
        return pharmacyService.delete(id);
    }
}
