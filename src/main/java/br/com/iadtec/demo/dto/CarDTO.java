package br.com.iadtec.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CarDTO {

    private Long brandId;
    private String modelName;
    private Long year;
    private BigDecimal basePrice;
}
