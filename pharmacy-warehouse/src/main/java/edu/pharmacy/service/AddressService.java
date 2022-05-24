package edu.pharmacy.service;

import edu.pharmacy.dao.AddressRepository;
import edu.pharmacy.dao.StreetRepository;
import edu.pharmacy.exception.BusinessLogicException;
import edu.pharmacy.model.dto.AddressFullInfoDTO;
import edu.pharmacy.model.dto.AddressToCreateDTO;
import edu.pharmacy.model.dto.AddressToUpdateDTO;
import edu.pharmacy.model.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressService extends AbstractService {

    private final AddressRepository addressRepository;
    private final StreetRepository streetRepository;
    private final PharmacyService pharmacyService;
    private final SupplierService supplierService;
    private final WarehouseService warehouseService;
    private final StreetService streetService;

    @SneakyThrows
    public AddressFullInfoDTO create(String addressToCreateJson) {
        AddressToCreateDTO addressToCreateDTO = objectMapper.readValue(addressToCreateJson, AddressToCreateDTO.class);
        long streetId = addressToCreateDTO.getStreetId();
        streetService.checkExistence(streetId);
        Street street = streetRepository.getById(streetId);
        String house = addressToCreateDTO.getHouse();
        Address address = new Address();
        address.setStreet(street);
        address.setHouse(house);
        Address createdAddress = addressRepository.save(address);
        long pharmacyId = addressToCreateDTO.getPharmacyId();
        long supplierId = addressToCreateDTO.getSupplierId();
        long warehouseId = addressToCreateDTO.getWarehouseId();
        if (pharmacyId != 0) {
            pharmacyService.setAddress(pharmacyId, createdAddress);
        }
        if (supplierId != 0) {
            supplierService.setAddress(supplierId, createdAddress);
        }
        if (warehouseId != 0) {
            warehouseService.setAddress(warehouseId, createdAddress);
        }
        //System.out.println(createdAddress.getPharmacy());
        return mapFromAddressToAddressFullInfoDTO(createdAddress);
    }

    public List<AddressFullInfoDTO> getAll() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream().map(this::mapFromAddressToAddressFullInfoDTO).toList();
    }

    public AddressFullInfoDTO get(long id) {
        checkExistence(id);
        Address address = addressRepository.getById(id);
        return mapFromAddressToAddressFullInfoDTO(address);
    }

    @SneakyThrows
    public AddressFullInfoDTO update(long id, String updatedAddressJson) {
        AddressToUpdateDTO addressToUpdateDTO = objectMapper.readValue(updatedAddressJson, AddressToUpdateDTO.class);
        checkExistence(id);
        Address addressToUpdate = addressRepository.getById(id);
        Address updatedAddress = updateAddressFields(addressToUpdate, addressToUpdateDTO);
        return mapFromAddressToAddressFullInfoDTO(addressRepository.save(updatedAddress));
    }

    public long delete(long id) {
        checkExistence(id);
        addressRepository.deleteById(id);
        return id;
    }

    private void checkExistence(long id) {
        if (!addressRepository.existsById(id)) throw new BusinessLogicException("Address with this id not found");
    }

    private Address updateAddressFields(Address addressToUpdate, AddressToUpdateDTO addressToUpdateDTO) {
        long streetId = addressToUpdateDTO.getStreetId();
        if (streetId != 0) {
            Street street = streetRepository.getById(streetId);
            addressToUpdate.setStreet(street);
        }
        String house = addressToUpdateDTO.getHouse();
        if (house != null) {
            addressToUpdate.setHouse(house);
        }
        return addressToUpdate;
    }

    private AddressFullInfoDTO mapFromAddressToAddressFullInfoDTO(Address address) {
        AddressFullInfoDTO addressFullInfoDTO = modelMapper.map(address, AddressFullInfoDTO.class);
        Street street = address.getStreet();
        City city = street.getCity();
        addressFullInfoDTO.setCityName(city.getName());
        addressFullInfoDTO.setStreetName(street.getName());
        if (address.getPharmacy() != null) {
            Pharmacy pharmacy = address.getPharmacy();
            addressFullInfoDTO.setOrganizationType("Pharmacy");
            addressFullInfoDTO.setOrganizationId(pharmacy.getId());
            addressFullInfoDTO.setOrganizationName(pharmacy.getName());
        }
        if (address.getSupplier() != null) {
            Supplier supplier = address.getSupplier();
            addressFullInfoDTO.setOrganizationType("Supplier");
            addressFullInfoDTO.setOrganizationId(supplier.getId());
            addressFullInfoDTO.setOrganizationName(supplier.getName());
        }
        if (address.getWarehouse() != null) {
            Warehouse warehouse = address.getWarehouse();
            addressFullInfoDTO.setOrganizationType("Warehouse");
            addressFullInfoDTO.setOrganizationId(warehouse.getId());
        }
        return addressFullInfoDTO;
    }
}
