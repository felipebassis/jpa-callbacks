package br.com.iadtec.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(staticName = "from")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class CarId implements Serializable {

    private static final long serialVersionUID = 8952167294523739165L;

    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BRAND_ID", nullable = false)
    private CarBrand brand;

    public CarId(CarBrand carBrand) {
        this.brand = carBrand;

    }
}
