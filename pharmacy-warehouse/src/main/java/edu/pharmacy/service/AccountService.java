package edu.pharmacy.service;

import edu.pharmacy.dao.AccountRepository;
import edu.pharmacy.exception.BusinessLogicException;
import edu.pharmacy.model.dto.AccountFullInfoDTO;
import edu.pharmacy.model.dto.AccountToCreateDTO;
import edu.pharmacy.model.dto.AccountToUpdateDTO;
import edu.pharmacy.model.entity.Account;
import edu.pharmacy.model.entity.Pharmacy;
import edu.pharmacy.model.entity.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService extends AbstractService {

    private final AccountRepository accountRepository;
    private final PharmacyService pharmacyService;
    private final SupplierService supplierService;
    private static final String IBAN_START_STRING = "BY";

    @SneakyThrows
    public AccountFullInfoDTO create(String accountJson) {
        AccountToCreateDTO accountToCreateDTO = objectMapper.readValue(accountJson, AccountToCreateDTO.class);
        Account accountToCreate = new Account();
        String generatedIban = generateIban();
        accountToCreate.setIban(generatedIban);
        accountToCreate.setBalance(0);
        Account createdAccount = accountRepository.save(accountToCreate);
        AccountFullInfoDTO accountFullInfoDTO;
        long pharmacyId = accountToCreateDTO.getPharmacyId();
        long supplierId = accountToCreateDTO.getSupplierId();
        if (pharmacyId != 0) {
            pharmacyService.setAccount(pharmacyId, createdAccount);
            accountFullInfoDTO = mapFromAccountToAccountFullInfoDTO(createdAccount);
        } else if (supplierId != 0) {
            supplierService.setAccount(supplierId, createdAccount);
            accountFullInfoDTO = mapFromAccountToAccountFullInfoDTO(createdAccount);
        } else throw new BusinessLogicException("Accounts holder id is missing");
        return accountFullInfoDTO;
    }

    public List<AccountFullInfoDTO> getAll() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(this::mapFromAccountToAccountFullInfoDTO).toList();
    }

    public AccountFullInfoDTO get(String iban) {
        checkExistence(iban);
        Account account = accountRepository.getById(iban);
        return mapFromAccountToAccountFullInfoDTO(account);
    }

    @SneakyThrows
    public AccountFullInfoDTO updateBalance(String iban, String updatedAccountJson) {
        AccountToUpdateDTO accountToUpdateDTO = objectMapper.readValue(updatedAccountJson, AccountToUpdateDTO.class);
        checkExistence(iban);
        Account accountToUpdate = accountRepository.getById(iban);
        Account updatedAccount = updateAccountFields(accountToUpdate, accountToUpdateDTO);
        return mapFromAccountToAccountFullInfoDTO(accountRepository.save(accountToUpdate));
    }

    public String delete(String iban) {
        checkExistence(iban);
        accountRepository.deleteById(iban);
        return iban;
    }

    private void checkExistence(String iban) {
        if (!accountRepository.existsById(iban)) throw new BusinessLogicException("Account with this id not found");
    }

    private Account updateAccountFields(Account account, AccountToUpdateDTO accountToUpdateDTO) {
        accountToUpdateDTO.setBalance(accountToUpdateDTO.getBalance());
        return account;
    }

    private AccountFullInfoDTO mapFromAccountToAccountFullInfoDTO(Account account) {
        AccountFullInfoDTO accountFullInfoDTO = modelMapper.map(account, AccountFullInfoDTO.class);
        if (account.getPharmacy() != null) {
            Pharmacy pharmacy = account.getPharmacy();
            accountFullInfoDTO.setOrganizationType("Pharmacy");
            accountFullInfoDTO.setOrganizationId(pharmacy.getId());
            accountFullInfoDTO.setOrganizationName(pharmacy.getName());
        } else if (account.getSupplier() != null) {
            Supplier supplier = account.getSupplier();
            accountFullInfoDTO.setOrganizationType("Supplier");
            accountFullInfoDTO.setOrganizationId(supplier.getId());
            accountFullInfoDTO.setOrganizationName(supplier.getName());
        }
        return accountFullInfoDTO;
    }

    private String generateIban() {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 26; i++) number.append((int) (Math.random() * 9));
        return IBAN_START_STRING + number;
    }
}
