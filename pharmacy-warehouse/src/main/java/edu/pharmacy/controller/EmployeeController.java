package edu.pharmacy.controller;

import edu.pharmacy.model.dto.EmployeeFullInfoDTO;
import edu.pharmacy.model.dto.StreetFullInfoDTO;
import edu.pharmacy.service.EmployeeService;
import edu.pharmacy.service.StreetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/staff")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public EmployeeFullInfoDTO create(@RequestBody String cityEmployee) {
        return employeeService.create(cityEmployee);
    }

    @GetMapping
    public List<EmployeeFullInfoDTO> getAll() {
        return employeeService.getAll();
    }

    @GetMapping(value = "{id}")
    public EmployeeFullInfoDTO get(@PathVariable("id") long id) {
        return employeeService.get(id);
    }

    @PutMapping(value = "{id}")
    public EmployeeFullInfoDTO update(@PathVariable("id") long id, @RequestBody String updatedEmployeeJson) {
        return employeeService.update(id, updatedEmployeeJson);
    }

    @DeleteMapping(value = "{id}")
    public long delete(@PathVariable("id") long id) {
        return employeeService.delete(id);
    }
}
