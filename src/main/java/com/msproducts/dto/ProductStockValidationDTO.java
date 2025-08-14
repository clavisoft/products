package com.msproducts.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductStockValidationDTO {
  private Long id;
  private String name;
  private Double price;
  private boolean stockAvailable;
  private Integer currentStock;
}