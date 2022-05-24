package edu.pharmacy.service;

import edu.pharmacy.dao.ProductRepository;
import edu.pharmacy.dao.SupplierRepository;
import edu.pharmacy.exception.BusinessLogicException;
import edu.pharmacy.model.dto.ProductFullInfoDTO;
import edu.pharmacy.model.dto.ProductToCreateDTO;
import edu.pharmacy.model.dto.ProductToUpdateDTO;
import edu.pharmacy.model.entity.Product;
import edu.pharmacy.model.entity.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService extends AbstractService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final SupplierService supplierService;

    @SneakyThrows
    public ProductFullInfoDTO create(String productJson) {
        ProductToCreateDTO productToCreateDTO = objectMapper.readValue(productJson, ProductToCreateDTO.class);
        long supplierId = productToCreateDTO.getSupplierId();
        supplierService.checkExistence(supplierId);
        Supplier supplier = supplierRepository.getById(supplierId);
        String name = productToCreateDTO.getName();
        checkName(name);
        String activeSubstanceName = productToCreateDTO.getActiveSubstanceName();
        double price = productToCreateDTO.getPrice();
        Product product = new Product();
        product.setSupplier(supplier);
        product.setName(name);
        product.setActiveSubstanceName(activeSubstanceName);
        product.setPrice(price);
        Product createdProduct = productRepository.save(product);
        return mapFromProductToProductFullInfoDTO(createdProduct);
    }

    public List<ProductFullInfoDTO> getAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapFromProductToProductFullInfoDTO).toList();
    }

    public ProductFullInfoDTO get(long id) {
        checkExistence(id);
        Product product = productRepository.getById(id);
        return mapFromProductToProductFullInfoDTO(product);
    }

    @SneakyThrows
    public ProductFullInfoDTO update(long id, String updatedProductJson) {
        ProductToUpdateDTO productToUpdateDTO = objectMapper.readValue(updatedProductJson, ProductToUpdateDTO.class);
        checkExistence(id);
        Product productToUpdate = productRepository.getById(id);
        Product updatedProduct = updateProductFields(productToUpdate, productToUpdateDTO);
        return mapFromProductToProductFullInfoDTO(productRepository.save(updatedProduct));
    }

    public long delete(long id) {
        checkExistence(id);
        productRepository.deleteById(id);
        return id;
    }

    public void checkExistence(long id) {
        if (!productRepository.existsById(id)) throw new BusinessLogicException("Product with this id not found");
    }

    private void checkName(String name) {
        if (productRepository.existsByName(name)) {
            throw new BusinessLogicException("Product with this name is already exists");
        }
        int length = name.length();
        if (length < 3 || length > 50) {
            throw new BusinessLogicException("The maximum length of the product name is 50 characters, and the minimum is 3 characters.");
        }
    }

    private Product updateProductFields(Product productToUpdate, ProductToUpdateDTO productToUpdateDTO) {
        String name = productToUpdateDTO.getName();
        if (name != null) {
            productToUpdate.setName(name);
        }
        String activeSubstanceName = productToUpdateDTO.getActiveSubstanceName();
        if (activeSubstanceName != null) {
            productToUpdate.setActiveSubstanceName(activeSubstanceName);
        }
        double price = productToUpdateDTO.getPrice();
        if (price != 0) {
            productToUpdate.setPrice(price);
        }
        return productToUpdate;
    }

    private ProductFullInfoDTO mapFromProductToProductFullInfoDTO(Product product) {
        ProductFullInfoDTO productFullInfoDTO = modelMapper.map(product, ProductFullInfoDTO.class);
        Supplier supplier = product.getSupplier();
        productFullInfoDTO.setSupplierId(supplier.getId());
        productFullInfoDTO.setSupplierName(supplier.getName());
        return productFullInfoDTO;
    }
}
