package com.msproducts.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductDTO {

  @NotBlank(message = "El nombre es obligatorio")
  private String name;

  @Size(max = 255, message = "La descripción no debe superar los 255 caracteres")
  private String description;

  @NotNull(message = "El precio es obligatorio")
  @Positive(message = "El precio debe ser mayor que cero")
  private Double price;

  @NotNull(message = "El stock es obligatorio")
  @Min(value = 0, message = "El stock no puede ser negativo")
  private Integer stock;

  @Size(max = 255, message = "La categoria no debe superar los 255 caracteres")
  private String category;

}
