package br.com.iadtec.demo.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "id", callSuper = false)
@ToString
@Entity
public class CarBrand extends Auditable<Long> {

    private static final long serialVersionUID = -7439626231854965576L;

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    public CarBrand(BrandDTO brandDTO) {
        this.id = brandDTO.getId();
        this.name = brandDTO.getName();
    }
}
