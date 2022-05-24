package edu.pharmacy.controller;

import edu.pharmacy.model.dto.PharmacyFullInfoDTO;
import edu.pharmacy.model.dto.ProductFullInfoDTO;
import edu.pharmacy.model.dto.SupplierFullInfoDTO;
import edu.pharmacy.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductFullInfoDTO create(@RequestBody String productJson) {
        return productService.create(productJson);
    }

    @GetMapping
    public List<ProductFullInfoDTO> getAll() {
        return productService.getAll();
    }

    @GetMapping(value = "{id}")
    public ProductFullInfoDTO get(@PathVariable("id") long id) {
        return productService.get(id);
    }

    @PutMapping(value = "{id}")
    public ProductFullInfoDTO update(@PathVariable("id") long id, @RequestBody String updatedProductJson) {
        return productService.update(id, updatedProductJson);
    }

    @DeleteMapping(value = "{id}")
    public long delete(@PathVariable("id") long id) {
        return productService.delete(id);
    }
}
