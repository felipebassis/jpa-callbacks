package br.com.iadtec.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModelDTO {

    private Long carBrandId;
    private String carBrandName;
    private String name;
}
