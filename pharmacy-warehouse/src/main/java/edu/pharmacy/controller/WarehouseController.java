package edu.pharmacy.controller;

import edu.pharmacy.model.dto.CityFullInfoDTO;
import edu.pharmacy.model.dto.WarehouseFullInfoDTO;
import edu.pharmacy.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    public WarehouseFullInfoDTO create(@RequestBody String warehouseJson) {
        return warehouseService.create(warehouseJson);
    }

    @GetMapping
    public List<WarehouseFullInfoDTO> getAll() {
        return warehouseService.getAll();
    }

    @GetMapping(value = "{id}")
    public WarehouseFullInfoDTO get(@PathVariable("id") long id) {
        return warehouseService.get(id);
    }

    @PutMapping(value = "{id}")
    public WarehouseFullInfoDTO update(@PathVariable("id") long id, @RequestBody String updatedWarehouseJson) {
        return warehouseService.update(id, updatedWarehouseJson);
    }

    @DeleteMapping(value = "{id}")
    public long delete(@PathVariable("id") long id) {
        return warehouseService.delete(id);
    }
}
