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

    @Getter
    @Column(name = "year")
    private Long year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
            @JoinColumn(name = "model_name", referencedColumnName = "name"),
            @JoinColumn(name = "car_brand_id", referencedColumnName = "car_brand_id")
    })
    private Model model;

    public Long getCarBrandId() {
        return model.getCarBrandId();
    }

    public String getModelName() {
        return model.getName();
    }
}
