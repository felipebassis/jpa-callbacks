package br.com.iadtec.demo.listener.message;

import br.com.iadtec.demo.entity.Auditable;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

@ToString
public class AuditoryUpdateMessage implements AuditoryMessage {

    private static final long serialVersionUID = 6423681020154321492L;

    @Getter
    private final Auditable<?> currentState;

    @Getter
    private final Map<String, Object> previousState;

    AuditoryUpdateMessage(Auditable<?> currentState, Map<String, Object> previousState) {
        this.currentState = currentState;
        this.previousState = Collections.unmodifiableMap(previousState);
    }

}
