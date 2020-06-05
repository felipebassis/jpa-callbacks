package br.com.iadtec.demo.util.entity.singlecolumnid;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@AllArgsConstructor
@Entity
public class CommonIdWithColumn {

    @Id
    @Column(name = "a_id")
    private final Long id;
}
