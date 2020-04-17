package br.com.iadtec.demo.entity;

import lombok.Data;

import java.util.List;

@Data
public class BrandBatchDTO {

    private List<BrandDTO> brands;
}
