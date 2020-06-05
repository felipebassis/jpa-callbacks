package br.com.iadtec.demo.listener.message;

import br.com.iadtec.demo.entity.Auditable;

import java.util.Map;

public final class AuditoryMessageFactory {

    public static AuditoryPersistMessage persistMessage(Auditable<?> currentState) {
        return new AuditoryPersistMessage(currentState);
    }

    public static AuditoryUpdateMessage updateMessage(Auditable<?> currentState, Map<String, Object> previousState) {
        return new AuditoryUpdateMessage(currentState, previousState);
    }

    public static AuditoryDeleteMessage deleteMessage(Map<String, Object> previousState) {
        return new AuditoryDeleteMessage(previousState);
    }
}
