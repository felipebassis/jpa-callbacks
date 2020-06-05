package br.com.iadtec.demo.listener;

import br.com.iadtec.demo.entity.Auditable;

public class AuditTriggerMessage {

    private Auditable<?> previousState;
    private Auditable<?> newState;

    private AuditTriggerMessage() {
    }

    public static AuditTriggerMessage persist(Auditable<?> newState) {
        AuditTriggerMessage message = new AuditTriggerMessage();
        message.newState = newState;
        return message;
    }

    public static AuditTriggerMessage update(Auditable<?> previousState, Auditable<?> newState) {
        AuditTriggerMessage message = new AuditTriggerMessage();
        message.previousState = previousState;
        message.newState = newState;
        return message;
    }

    public static AuditTriggerMessage delete(Auditable<?> previousState) {
        AuditTriggerMessage message = new AuditTriggerMessage();
        message.previousState = previousState;
        return message;
    }
}
