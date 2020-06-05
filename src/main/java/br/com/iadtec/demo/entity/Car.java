package br.com.iadtec.demo.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
@Table(name = "TB_CAR")
public class Car extends Auditable<CarId> {

    private static final long serialVersionUID = -8816894757887525745L;

    @Getter
    @EmbeddedId
    private CarId id;

    @Getter
    @Setter
    @Column(name = "base_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal basePrice;

    public Long getYear() {
        return id.getYear();
    }

    public Long getCarBrandId() {
        return id.getCarBrandId();
    }

    public String getModelName() {
        return id.getModelName();
    }
}
