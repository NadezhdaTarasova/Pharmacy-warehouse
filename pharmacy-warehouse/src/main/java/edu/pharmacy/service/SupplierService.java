package edu.pharmacy.service;

import edu.pharmacy.dao.SupplierRepository;
import edu.pharmacy.exception.BusinessLogicException;
import edu.pharmacy.model.dto.SupplierFullInfoDTO;
import edu.pharmacy.model.dto.SupplierToCreateDTO;
import edu.pharmacy.model.dto.SupplierToUpdateDTO;
import edu.pharmacy.model.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SupplierService extends AbstractService {

    private final SupplierRepository supplierRepository;

    @SneakyThrows
    public SupplierFullInfoDTO create(String supplierToCreateJson) {
        SupplierToCreateDTO supplierToCreateDTO = objectMapper.readValue(supplierToCreateJson, SupplierToCreateDTO.class);
        String name = supplierToCreateDTO.getName();
        String phone = supplierToCreateDTO.getPhone();
        checkName(name);
        checkPhone(phone);
        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setPhone(phone);
        Supplier createdSupplier = supplierRepository.save(supplier);
        return mapFromSupplierToSupplierFullInfoDTO(createdSupplier);
    }

    public List<SupplierFullInfoDTO> getAll() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers.stream().map(this::mapFromSupplierToSupplierFullInfoDTO).toList();
    }

    public SupplierFullInfoDTO get(long id) {
        checkExistence(id);
        Supplier supplier = supplierRepository.getById(id);
        return mapFromSupplierToSupplierFullInfoDTO(supplier);
    }

    @SneakyThrows
    public SupplierFullInfoDTO update(long id, String updatedSupplerJson) {
        SupplierToUpdateDTO supplierToUpdateDTO = objectMapper.readValue(updatedSupplerJson, SupplierToUpdateDTO.class);
        checkExistence(id);
        Supplier supplierToUpdate = supplierRepository.getById(id);
        Supplier updatedSupplier = updateSupplierFields(supplierToUpdate, supplierToUpdateDTO);
        return mapFromSupplierToSupplierFullInfoDTO(supplierRepository.save(updatedSupplier));
    }

    public long delete(long id) {
        checkExistence(id);
        supplierRepository.deleteById(id);
        return id;
    }

    public void setAccount(long id, Account account) {
        checkExistence(id);
        Supplier supplier = supplierRepository.getById(id);
        supplier.setAccount(account);
        supplierRepository.save(supplier);
    }

    public void setAddress(long id, Address address) {
        checkExistence(id);
        Supplier supplier = supplierRepository.getById(id);
        supplier.setAddress(address);
        supplierRepository.save(supplier);
    }

    public void checkExistence(long id) {
        if (!supplierRepository.existsById(id)) {
            throw new BusinessLogicException("Supplier with this id is not found");
        }
    }

    private void checkName(String name) {
        if (supplierRepository.existsByName(name))
            throw new BusinessLogicException("Supplier with this name is already exists");
        int length = name.length();
        if (length < 3 || length > 50) {
            throw new BusinessLogicException("The maximum length of the supplier name is 50 characters, and the minimum is 3 characters.");
        }
    }

    private void checkPhone(String phone) {
        if (supplierRepository.existsByPhone(phone))
            throw new BusinessLogicException("Supplier with this phone is already exists");
        int length = phone.length();
        if (length < 7 || length > 13) {
            throw new BusinessLogicException("The maximum length of the supplier phone is 13 characters, and the minimum is 7 characters.");
        }
    }

    private Supplier updateSupplierFields(Supplier supplierToUpdate, SupplierToUpdateDTO supplierToUpdateDTO) {
        String name = supplierToUpdateDTO.getName();
        if (name != null) {
            checkName(name);
            supplierToUpdate.setName(name);
        }
        String phone = supplierToUpdateDTO.getPhone();
        if (phone != null) {
            checkPhone(phone);
            supplierToUpdate.setPhone(phone);
        }
        return supplierToUpdate;
    }

    private SupplierFullInfoDTO mapFromSupplierToSupplierFullInfoDTO(Supplier supplier) {
        SupplierFullInfoDTO supplierToUpdateDTO = modelMapper.map(supplier, SupplierFullInfoDTO.class);
        Address address = supplier.getAddress();
        if (address != null) {
            Street street = address.getStreet();
            City city = street.getCity();
            supplierToUpdateDTO.setCityName(city.getName());
            supplierToUpdateDTO.setStreetName(street.getName());
            supplierToUpdateDTO.setHouse(address.getHouse());
        }
        Account account = supplier.getAccount();
        if (account != null) {
            supplierToUpdateDTO.setAccountIban(account.getIban());
            supplierToUpdateDTO.setAccountBalance(account.getBalance());
        }
        return supplierToUpdateDTO;
    }
}
