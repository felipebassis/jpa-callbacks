package br.com.iadtec.demo.util.entity.embeddedid;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@Entity
public class EmbeddedIdWithEmbeddedField {

    @EmbeddedId
    private final Id id;

    @Getter
    @AllArgsConstructor
    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = -7439971295300315184L;
        private final String string;

        @Embedded
        private final EmbeddedClass embeddedClass;

        @Getter
        @AllArgsConstructor
        @Embeddable
        public static class EmbeddedClass {

            @Column(name = "another_string")
            private final String string;

            @Transient
            private final Long aLong;
        }
    }
}
