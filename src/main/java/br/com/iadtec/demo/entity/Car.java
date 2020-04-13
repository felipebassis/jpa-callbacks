package br.com.iadtec.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@EqualsAndHashCode(exclude = "id", callSuper = false)
@ToString(exclude = "brand")
@Entity
@Table(name = "TB_CAR")
public class Car extends Auditable<UUID> {

    private static final long serialVersionUID = -8816894757887525745L;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BRAND_ID", nullable = false)
    private Brand brand;
}
