package br.com.iadtec.demo.listener.message;

import br.com.iadtec.demo.entity.Auditable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class AuditoryPersistMessage implements AuditoryMessage {
    private static final long serialVersionUID = 710735413023636069L;

    @Getter
    private final Auditable<?> currentState;
}
