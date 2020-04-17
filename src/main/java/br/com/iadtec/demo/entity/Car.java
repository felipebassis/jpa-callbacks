package br.com.iadtec.demo.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "id", callSuper = false)
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
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Getter
    @Setter
    @Column(name = "YEAR", nullable = false)
    private Long year;

    public Car(CarId id) {
        this.id = id;
    }
}
