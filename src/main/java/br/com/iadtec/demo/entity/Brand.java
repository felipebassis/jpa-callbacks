package br.com.iadtec.demo.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@NoArgsConstructor
@EqualsAndHashCode(exclude = "id", callSuper = false)
@ToString
@Entity
@Table(name = "TB_BRAND")
public class Brand extends Auditable<UUID> {

    private static final long serialVersionUID = -7439626231854965576L;

    @Getter
    @Id
    private UUID id = UUID.randomUUID();

    @Getter
    @Setter
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
