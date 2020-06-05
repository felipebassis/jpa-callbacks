package br.com.iadtec.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "from")
@Embeddable
public class ModelId implements Serializable {

    private static final long serialVersionUID = -313919935427065343L;

    @Getter
    @Column(name = "name")
    private String name;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_brand_id", referencedColumnName = "id")
    private CarBrand carBrand;

    public Long getCarBrandId() {
        return carBrand.getId();
    }

    public String getCarBrandName() {
        return carBrand.getName();
    }
}
