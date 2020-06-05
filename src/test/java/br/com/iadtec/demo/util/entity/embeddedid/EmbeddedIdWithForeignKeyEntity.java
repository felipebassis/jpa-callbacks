package br.com.iadtec.demo.util.entity.embeddedid;


import br.com.iadtec.demo.util.entity.singlecolumnid.CommonIdEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@Entity
public class EmbeddedIdWithForeignKeyEntity {

    @EmbeddedId
    private final Id id;

    @Getter
    @AllArgsConstructor
    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = 3621256313070888978L;
        private final String string;

        @OneToOne
        private final CommonIdEntity commonIdEntity;
    }
}
