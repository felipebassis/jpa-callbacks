package br.com.iadtec.demo.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = {"id"}, callSuper = false)
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
    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
