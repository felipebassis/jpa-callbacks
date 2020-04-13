package br.com.iadtec.demo.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class CarDTO {

    private UUID id;
    private String name;
    private Long year;
    private UUID brandId;
}
