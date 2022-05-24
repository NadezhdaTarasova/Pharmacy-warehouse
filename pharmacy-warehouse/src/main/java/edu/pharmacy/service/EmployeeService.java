package edu.pharmacy.service;

import edu.pharmacy.dao.EmployeeRepository;
import edu.pharmacy.dao.PharmacyRepository;
import edu.pharmacy.exception.BusinessLogicException;
import edu.pharmacy.model.dto.EmployeeFullInfoDTO;
import edu.pharmacy.model.dto.EmployeeToCreateDTO;
import edu.pharmacy.model.dto.EmployeeToUpdateDTO;
import edu.pharmacy.model.entity.Employee;
import edu.pharmacy.model.entity.Pharmacy;
import edu.pharmacy.model.entity.Warehouse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService extends AbstractService {

    private final EmployeeRepository employeeRepository;
    private final PharmacyRepository pharmacyRepository;
    private final PharmacyService pharmacyService;

    @SneakyThrows
    public EmployeeFullInfoDTO create(String employeeJson) {
        EmployeeToCreateDTO employeeToCreateDTO = objectMapper.readValue(employeeJson, EmployeeToCreateDTO.class);
        long pharmacyId = employeeToCreateDTO.getPharmacyId();
        pharmacyService.checkExistence(pharmacyId);
        Pharmacy pharmacy = pharmacyRepository.getById(pharmacyId);
        String firstName = employeeToCreateDTO.getFirstName();
        String lastName = employeeToCreateDTO.getLastName();
        String patronymic = employeeToCreateDTO.getPatronymic();
        String position = employeeToCreateDTO.getPosition();
        LocalDate dateOfBirth = employeeToCreateDTO.getDateOfBirth();
        String phone = employeeToCreateDTO.getPhone();
        checkPhone(phone);
        String email = employeeToCreateDTO.getEmail();
        checkEmail(email);
        Employee employee = new Employee();
        employee.setPharmacy(pharmacy);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setPatronymic(patronymic);
        employee.setPosition(position);
        employee.setDateOfBirth(dateOfBirth);
        employee.setPhone(phone);
        employee.setEmail(email);
        Employee createdEmployee = employeeRepository.save(employee);
        return mapFromEmployeeToEmployeeFullInfoDTO(createdEmployee);
    }

    public List<EmployeeFullInfoDTO> getAll() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(this::mapFromEmployeeToEmployeeFullInfoDTO).toList();
    }

    public EmployeeFullInfoDTO get(long id) {
        checkExistence(id);
        Employee employee = employeeRepository.getById(id);
        return mapFromEmployeeToEmployeeFullInfoDTO(employee);
    }

    @SneakyThrows
    public EmployeeFullInfoDTO update(long id, String updatedEmployeeJson) {
        EmployeeToUpdateDTO employeeToUpdateDTO = objectMapper.readValue(updatedEmployeeJson, EmployeeToUpdateDTO.class);
        checkExistence(id);
        Employee employeeToUpdate = employeeRepository.getById(id);
        Employee updatedEmployee = updateEmployeeFields(employeeToUpdate, employeeToUpdateDTO);
        return mapFromEmployeeToEmployeeFullInfoDTO(employeeRepository.save(updatedEmployee));
    }

    public long delete(long id) {
        checkExistence(id);
        employeeRepository.deleteById(id);
        return id;
    }

    public void checkExistence(long id) {
        if (!employeeRepository.existsById(id)) throw new BusinessLogicException("Employee with this id not found");
    }

    private void checkPhone(String phone) {
        if (employeeRepository.existsByPhone(phone))
            throw new BusinessLogicException("Employee with this phone is already exists");
        int length = phone.length();
        if (length < 7 || length > 13) {
            throw new BusinessLogicException("The maximum length of the phone name is 13 characters, and the minimum is 7 characters.");
        }
    }

    private void checkEmail(String email) {
        if (employeeRepository.existsByEmail(email))
            throw new BusinessLogicException("Employee with this phone is already exists");
        int length = email.length();
        if (length < 5 || length > 50) {
            throw new BusinessLogicException("The maximum length of the email name is 5 characters, and the minimum is 50 characters.");
        }
    }

    private Employee updateEmployeeFields(Employee employeeToUpdate, EmployeeToUpdateDTO employeeToUpdateDTO) {
        String firstName = employeeToUpdateDTO.getFirstName();
        if (firstName != null) {
            employeeToUpdate.setFirstName(firstName);
        }
        String lastName = employeeToUpdateDTO.getLastName();
        if (lastName != null) {
            employeeToUpdate.setLastName(lastName);
        }
        String patronymic = employeeToUpdateDTO.getPatronymic();
        if (patronymic != null) {
            employeeToUpdate.setPatronymic(patronymic);
        }
        LocalDate dateOfBirth = employeeToUpdateDTO.getDateOfBirth();
        if (dateOfBirth != null) {
            employeeToUpdate.setDateOfBirth(dateOfBirth);
        }
        String phone = employeeToUpdateDTO.getPhone();
        if (phone != null) {
            checkPhone(phone);
            employeeToUpdate.setPhone(phone);
        }
        String email = employeeToUpdateDTO.getEmail();
        if (email != null) {
            checkEmail(email);
            employeeToUpdate.setEmail(email);
        }
        return employeeToUpdate;
    }

    private EmployeeFullInfoDTO mapFromEmployeeToEmployeeFullInfoDTO(Employee employee) {
        EmployeeFullInfoDTO employeeFullInfoDTO = modelMapper.map(employee, EmployeeFullInfoDTO.class);
        Pharmacy pharmacy = employee.getPharmacy();
        employeeFullInfoDTO.setPharmacyId(pharmacy.getId());
        employeeFullInfoDTO.setPharmacyName(pharmacy.getName());
        Warehouse warehouse = employee.getWarehouse();
        if (warehouse != null) {
            employeeFullInfoDTO.setResponsibleForWarehouse(true);
            employeeFullInfoDTO.setWarehouseId(warehouse.getId());
        }
        return employeeFullInfoDTO;
    }
}
