package br.com.iadtec.demo.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class CarDTO {

    private Long id;
    private Long brandId;
    private String name;
    private Long year;
}
