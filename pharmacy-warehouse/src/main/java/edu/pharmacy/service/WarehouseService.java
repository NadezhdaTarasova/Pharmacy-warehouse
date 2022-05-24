package edu.pharmacy.service;

import edu.pharmacy.dao.EmployeeRepository;
import edu.pharmacy.dao.PharmacyRepository;
import edu.pharmacy.dao.WarehouseRepository;
import edu.pharmacy.exception.BusinessLogicException;
import edu.pharmacy.model.dto.WarehouseFullInfoDTO;
import edu.pharmacy.model.dto.WarehouseToCreateDTO;
import edu.pharmacy.model.dto.WarehouseToUpdateDTO;
import edu.pharmacy.model.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseService extends AbstractService {

    private final WarehouseRepository warehouseRepository;
    private final PharmacyRepository pharmacyRepository;
    private final EmployeeRepository employeeRepository;
    private final PharmacyService pharmacyService;
    private final EmployeeService employeeService;

    @SneakyThrows
    public WarehouseFullInfoDTO create(String warehouseToCreateJson) {
        WarehouseToCreateDTO warehouseToCreateDTO = objectMapper.readValue(warehouseToCreateJson, WarehouseToCreateDTO.class);
        long pharmacyId = warehouseToCreateDTO.getPharmacyId();
        pharmacyService.checkExistence(pharmacyId);
        Pharmacy pharmacy = pharmacyRepository.getById(pharmacyId);
        long employeeId = warehouseToCreateDTO.getEmployeeId();
        employeeService.checkExistence(employeeId);
        Employee employee = employeeRepository.getById(employeeId);
        String phone = warehouseToCreateDTO.getPhone();
        checkPhone(phone);
        Warehouse warehouse = new Warehouse();
        warehouse.setPharmacy(pharmacy);
        warehouse.setEmployee(employee);
        warehouse.setPhone(phone);
        Warehouse createdWarehouse = warehouseRepository.save(warehouse);
        return mapFromWarehouseToWarehouseFullInfoDTO(createdWarehouse);
    }

    public List<WarehouseFullInfoDTO> getAll() {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        return warehouses.stream().map(this::mapFromWarehouseToWarehouseFullInfoDTO).toList();
    }

    public WarehouseFullInfoDTO get(long id) {
        checkExistence(id);
        Warehouse warehouse = warehouseRepository.getById(id);
        return mapFromWarehouseToWarehouseFullInfoDTO(warehouse);
    }

    @SneakyThrows
    public WarehouseFullInfoDTO update(long id, String updatedWarehouseJson) {
        WarehouseToUpdateDTO warehouseToUpdateDTO = objectMapper.readValue(updatedWarehouseJson, WarehouseToUpdateDTO.class);
        checkExistence(id);
        Warehouse warehouseToUpdate = warehouseRepository.getById(id);
        Warehouse updatedWarehouse = updatePharmacyFields(warehouseToUpdate, warehouseToUpdateDTO);
        return mapFromWarehouseToWarehouseFullInfoDTO(warehouseRepository.save(updatedWarehouse));
    }

    public long delete(long id) {
        checkExistence(id);
        warehouseRepository.deleteById(id);
        return id;
    }

    public void setAddress(long id, Address address) {
        checkExistence(id);
        Warehouse warehouse = warehouseRepository.getById(id);
        warehouse.setAddress(address);
        warehouseRepository.save(warehouse);
    }

    public WarehouseFullInfoDTO setEmployee(long id, long employeeId) {
        checkExistence(id);
        Warehouse warehouse = warehouseRepository.getById(id);
        employeeService.checkExistence(id);
        Employee employee = employeeRepository.getById(employeeId);
        warehouse.setEmployee(employee);
        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
        return mapFromWarehouseToWarehouseFullInfoDTO(updatedWarehouse);
    }

    public void checkExistence(long id) {
        if (!warehouseRepository.existsById(id)) {
            throw new BusinessLogicException("Warehouse with this id is not found");
        }
    }

    private void checkPhone(String phone) {
        if (warehouseRepository.existsByPhone(phone))
            throw new BusinessLogicException("Warehouse with this phone is already exists");
        int length = phone.length();
        if (length < 7 || length > 13) {
            throw new BusinessLogicException("The maximum length of the warehouse phone is 13 characters, and the minimum is 7 characters.");
        }
    }

    private Warehouse updatePharmacyFields(Warehouse warehouseToUpdate, WarehouseToUpdateDTO warehouseToUpdateDTO) {
        String phone = warehouseToUpdateDTO.getPhone();
        if (phone != null) {
            checkPhone(phone);
            warehouseToUpdate.setPhone(phone);
        }
        return warehouseToUpdate;
    }

    private WarehouseFullInfoDTO mapFromWarehouseToWarehouseFullInfoDTO(Warehouse warehouse) {
        WarehouseFullInfoDTO warehouseFullInfoDTO = modelMapper.map(warehouse, WarehouseFullInfoDTO.class);
        Pharmacy pharmacy = warehouse.getPharmacy();
        if (pharmacy != null) {
            warehouseFullInfoDTO.setPharmacyId(pharmacy.getId());
            warehouseFullInfoDTO.setPharmacyName(pharmacy.getName());
        }
        Address address = warehouse.getAddress();
        if (address != null) {
            Street street = address.getStreet();
            City city = street.getCity();
            warehouseFullInfoDTO.setCityName(city.getName());
            warehouseFullInfoDTO.setStreetName(street.getName());
            warehouseFullInfoDTO.setHouse(address.getHouse());
        }
        Employee employee = warehouse.getEmployee();
        if (employee != null) {
            warehouseFullInfoDTO.setResponsibleEmployeeId(employee.getId());
            warehouseFullInfoDTO.setResponsibleEmployeeName(employee.getFirstName() + " " + employee.getLastName() + " " + employee.getPatronymic());
        }
        return warehouseFullInfoDTO;
    }
}
