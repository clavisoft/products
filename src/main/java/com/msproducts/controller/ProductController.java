package com.msproducts.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msproducts.dto.ProductDTO;
import com.msproducts.dto.ProductStockValidationDTO;
import com.msproducts.service.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
      this.productService = productService;
    }

@GetMapping("")
public ResponseEntity<List<ProductDTO>> listAll() {
  List<ProductDTO> products = productService.listAll();
  
  if (products.isEmpty()) {
    return ResponseEntity.noContent().build();
  }

  return ResponseEntity.ok(products);
}

@GetMapping("/{id}")
public ResponseEntity<ProductDTO> getById(@PathVariable @Min(1) Long id) {
  ProductDTO dto = productService.findById(id);
  return ResponseEntity.ok(dto);
}

@GetMapping("/search")
public ResponseEntity<Object> findByName(
  @RequestParam @NotBlank(message = "El parámetro 'name' es obligatorio") String name,
  @RequestParam(defaultValue = "0") @Min(0) int page,
  @RequestParam(defaultValue = "5") @Min(1) int size
) {
  Page<ProductDTO> result = productService.findByName(name, page, size);
  if (result.isEmpty()) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(Map.of("message", "No se encontraron resultados para: " + name));
  }
  return ResponseEntity.ok(result);
}

@PostMapping("")
public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO productDto) {
  ProductDTO savedDto = productService.create(productDto);
  return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
}

@PutMapping("/{id}")
public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
    ProductDTO updated = productService.update(id, dto);
    return ResponseEntity.ok(updated);
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    productService.deleteById(id);
    return ResponseEntity.noContent().build();
}

@GetMapping("/{id}/validate")
public ResponseEntity<ProductStockValidationDTO> validateStock(
    @PathVariable Long id,
    @RequestParam int quantity) {

  ProductStockValidationDTO response = productService.validateStock(id, quantity);
  return ResponseEntity.ok(response);
}

@PutMapping("/{id}/decrease-stock")
public ResponseEntity<String> decreaseStock(
    @PathVariable Long id,
    @RequestParam int quantity) {

  productService.decreaseStock(id, quantity);
  return ResponseEntity.ok("Stock actualizado correctamente");
}

}
