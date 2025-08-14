package com.msproducts.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.msproducts.dto.ProductDTO;
import com.msproducts.dto.ProductStockValidationDTO;
import com.msproducts.exception.ResourceNotFoundException;
import com.msproducts.model.Product;
import com.msproducts.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

  private static final String NOT_FOUND_MSG = "Producto no encontrado con ID: ";

  private final ProductRepository repository;
  private final ModelMapper modelMapper;

  public Product convertToEntity(ProductDTO dto) {
    return modelMapper.map(dto, Product.class);
  }

  public ProductDTO convertToDTO(Product entity) {
    return modelMapper.map(entity, ProductDTO.class);
  }

  public ProductDTO create(ProductDTO dto) {
    Product entity = Product.builder()
        .name(dto.getName())
        .description(dto.getDescription())
        .price(dto.getPrice())
        .stock(dto.getStock())
        .category(dto.getCategory())
        .build();

    Product saved = repository.save(entity);
    return convertToDTO(saved);
  }

  public List<ProductDTO> listAll() {
    return repository.findAll()
        .stream()
        .map(this::convertToDTO)
        .toList();
  }

  public ProductDTO findById(Long id) {
    Product product = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MSG + id));
    return convertToDTO(product);
  }

  public ProductDTO update(Long id, ProductDTO dto) {
    Product existing = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MSG + id));

    existing.setName(dto.getName());
    existing.setDescription(dto.getDescription());
    existing.setPrice(dto.getPrice());
    existing.setStock(dto.getStock());
    existing.setCategory(dto.getCategory());

    Product updated = repository.save(existing);
    return convertToDTO(updated);
  }

  public void deleteById(Long id) {
    if (!repository.existsById(id)) {
      throw new ResourceNotFoundException(NOT_FOUND_MSG + id);
    }
    repository.deleteById(id);
  }

  public Page<ProductDTO> findByName(String name, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Product> productPage = repository.findByNameContainingIgnoreCase(name, pageable);
    return productPage.map(this::convertToDTO);
  }

  public ProductStockValidationDTO validateStock(Long id, int quantity) {
    Product product = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MSG + id));

    return ProductStockValidationDTO.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .currentStock(product.getStock())
        .stockAvailable(product.getStock() >= quantity)
        .build();
  }

  public void decreaseStock(Long id, int quantity) {
    Product product = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MSG + id));

    if (product.getStock() < quantity) {
      throw new IllegalArgumentException("Stock insuficiente para el producto con ID: " + id);
    }

    product.setStock(product.getStock() - quantity);
    repository.save(product);
  }
}
