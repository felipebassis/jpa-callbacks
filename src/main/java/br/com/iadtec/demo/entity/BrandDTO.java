package br.com.iadtec.demo.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class BrandDTO {

    private UUID id;
    private String name;
}
