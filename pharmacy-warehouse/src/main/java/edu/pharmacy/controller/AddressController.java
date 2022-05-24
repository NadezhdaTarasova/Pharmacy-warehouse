package edu.pharmacy.controller;

import edu.pharmacy.model.dto.AddressFullInfoDTO;
import edu.pharmacy.model.dto.AddressToCreateDTO;
import edu.pharmacy.model.dto.StreetFullInfoDTO;
import edu.pharmacy.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public AddressFullInfoDTO create(@RequestBody String cityAddress) {
        return addressService.create(cityAddress);
    }

    @GetMapping
    public List<AddressFullInfoDTO> getAll() {
        return addressService.getAll();
    }

    @GetMapping(value = "{id}")
    public AddressFullInfoDTO get(@PathVariable("id") long id) {
        return addressService.get(id);
    }

    @PutMapping(value = "{id}")
    public AddressFullInfoDTO update(@PathVariable("id") long id, @RequestBody String updatedAddressJson) {
        return addressService.update(id, updatedAddressJson);
    }

    @DeleteMapping(value = "{id}")
    public long delete(@PathVariable("id") long id) {
        return addressService.delete(id);
    }
}
