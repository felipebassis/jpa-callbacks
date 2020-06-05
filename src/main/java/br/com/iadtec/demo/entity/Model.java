package br.com.iadtec.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "tb_model")
public class Model extends Auditable<ModelId> {

    private static final long serialVersionUID = -8895564810499910820L;

    @Getter
    @EmbeddedId
    private ModelId id;

    @Builder.Default
    @Getter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "id.model")
    private List<Car> cars = new ArrayList<>();

    public String getName() {
        return id.getName();
    }

    public Long getCarBrandId() {
        return id.getCarBrandId();
    }

    public String getCarBrandName() {
        return id.getCarBrandName();
    }
}
