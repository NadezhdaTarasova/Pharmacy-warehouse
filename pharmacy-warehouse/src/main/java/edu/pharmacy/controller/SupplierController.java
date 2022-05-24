package edu.pharmacy.controller;

import edu.pharmacy.model.dto.StreetFullInfoDTO;
import edu.pharmacy.model.dto.SupplierFullInfoDTO;
import edu.pharmacy.model.dto.SupplierToCreateDTO;
import edu.pharmacy.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public SupplierFullInfoDTO create(@RequestBody String citySupplier) {
        return supplierService.create(citySupplier);
    }

    @GetMapping
    public List<SupplierFullInfoDTO> getAll() {
        return supplierService.getAll();
    }

    @GetMapping(value = "{id}")
    public SupplierFullInfoDTO get(@PathVariable("id") long id) {
        return supplierService.get(id);
    }

    @PutMapping(value = "{id}")
    public SupplierFullInfoDTO update(@PathVariable("id") long id, @RequestBody String updatedSupplierJson) {
        return supplierService.update(id, updatedSupplierJson);
    }

    @DeleteMapping(value = "{id}")
    public long delete(@PathVariable("id") long id) {
        return supplierService.delete(id);
    }
}
