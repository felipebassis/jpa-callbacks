package br.com.iadtec.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Table(name = "TB_CAR")
public class Car {

    @Getter
    @Id
    private UUID id = UUID.randomUUID();

    @Getter
    @Setter
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Getter
    @Setter
    @Column(name = "YEAR", nullable = false)
    private Long year;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "BRAND_ID", nullable = false)
    private Brand brand;
}
