package edu.pharmacy.service;

import edu.pharmacy.dao.PharmacyRepository;
import edu.pharmacy.exception.BusinessLogicException;
import edu.pharmacy.model.dto.PharmacyFullInfoDTO;
import edu.pharmacy.model.dto.PharmacyToCreateDTO;
import edu.pharmacy.model.dto.PharmacyToUpdateDTO;
import edu.pharmacy.model.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PharmacyService extends AbstractService {

    private final PharmacyRepository pharmacyRepository;

    @SneakyThrows
    public PharmacyFullInfoDTO create(String pharmacyToCreateJson) {
        PharmacyToCreateDTO pharmacyToCreateDTO = objectMapper.readValue(pharmacyToCreateJson, PharmacyToCreateDTO.class);
        String name = pharmacyToCreateDTO.getName();
        String phone = pharmacyToCreateDTO.getPhone();
        checkName(name);
        checkPhone(phone);
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setName(name);
        pharmacy.setPhone(phone);
        Pharmacy createdPharmacy = pharmacyRepository.save(pharmacy);
        return mapFromPharmacyToPharmacyFullInfoDTO(createdPharmacy);
    }

    public List<PharmacyFullInfoDTO> getAll() {
        List<Pharmacy> pharmacies = pharmacyRepository.findAll();
        return pharmacies.stream().map(this::mapFromPharmacyToPharmacyFullInfoDTO).toList();
    }

    public PharmacyFullInfoDTO get(long id) {
        checkExistence(id);
        Pharmacy pharmacy = pharmacyRepository.getById(id);
        return mapFromPharmacyToPharmacyFullInfoDTO(pharmacy);
    }

    @SneakyThrows
    public PharmacyFullInfoDTO update(long id, String updatedPharmacyJson) {
        PharmacyToUpdateDTO pharmacyToUpdateDTO = objectMapper.readValue(updatedPharmacyJson, PharmacyToUpdateDTO.class);
        checkExistence(id);
        Pharmacy pharmacyToUpdate = pharmacyRepository.getById(id);
        Pharmacy updatedPharmacy = updatePharmacyFields(pharmacyToUpdate, pharmacyToUpdateDTO);
        return mapFromPharmacyToPharmacyFullInfoDTO(pharmacyRepository.save(updatedPharmacy));
    }

    public long delete(long id) {
        checkExistence(id);
        pharmacyRepository.deleteById(id);
        return id;
    }

    public void setAccount(long id, Account account) {
        checkExistence(id);
        Pharmacy pharmacy = pharmacyRepository.getById(id);
        pharmacy.setAccount(account);
        pharmacyRepository.save(pharmacy);
    }

    public void setAddress(long id, Address address) {
        checkExistence(id);
        Pharmacy pharmacy = pharmacyRepository.getById(id);
        pharmacy.setAddress(address);
        pharmacyRepository.save(pharmacy);
    }

    public void checkExistence(long id) {
        if (!pharmacyRepository.existsById(id)) {
            throw new BusinessLogicException("Pharmacy with this id is not found");
        }
    }

    private void checkName(String name) {
        if (pharmacyRepository.existsByName(name)) {
            throw new BusinessLogicException("Pharmacy with this name is already exists");
        }
        int length = name.length();
        if (length < 3 || length > 50) {
            throw new BusinessLogicException("The maximum length of the pharmacy name is 50 characters, and the minimum is 3 characters.");
        }
    }

    private void checkPhone(String phone) {
        if (pharmacyRepository.existsByPhone(phone))
            throw new BusinessLogicException("Pharmacy with this phone is already exists");
        int length = phone.length();
        if (length < 7 || length > 13) {
            throw new BusinessLogicException("The maximum length of the phone name is 13 characters, and the minimum is 7 characters.");
        }
    }

    private Pharmacy updatePharmacyFields(Pharmacy pharmacyToUpdate, PharmacyToUpdateDTO pharmacyToUpdateDTO) {
        String name = pharmacyToUpdateDTO.getName();
        if (name != null) {
            checkName(name);
            pharmacyToUpdate.setName(name);
        }
        String phone = pharmacyToUpdateDTO.getPhone();
        if (phone != null) {
            checkPhone(phone);
            pharmacyToUpdate.setPhone(phone);
        }
        return pharmacyToUpdate;
    }

    private PharmacyFullInfoDTO mapFromPharmacyToPharmacyFullInfoDTO(Pharmacy pharmacy) {
        PharmacyFullInfoDTO pharmacyFullInfoDTO = modelMapper.map(pharmacy, PharmacyFullInfoDTO.class);
        Address address = pharmacy.getAddress();
        if (address != null) {
            Street street = address.getStreet();
            City city = street.getCity();
            pharmacyFullInfoDTO.setCityName(city.getName());
            pharmacyFullInfoDTO.setStreetName(street.getName());
            pharmacyFullInfoDTO.setHouse(address.getHouse());
        }
        Account account = pharmacy.getAccount();
        if (account != null) {
            pharmacyFullInfoDTO.setAccountIban(account.getIban());
            pharmacyFullInfoDTO.setAccountBalance(account.getBalance());
        }
        Warehouse warehouse = pharmacy.getWarehouse();
        if (warehouse != null) {
            pharmacyFullInfoDTO.setWarehouseId(warehouse.getId());
        }
        return pharmacyFullInfoDTO;
    }
}
