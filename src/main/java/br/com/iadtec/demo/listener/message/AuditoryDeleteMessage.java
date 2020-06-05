package br.com.iadtec.demo.listener.message;

import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

@ToString
public class AuditoryDeleteMessage implements AuditoryMessage {

    private static final long serialVersionUID = 6751917710304837514L;

    @Getter
    private final Map<String, Object> previousState;

    public AuditoryDeleteMessage(Map<String, Object> previousState) {
        this.previousState = Collections.unmodifiableMap(previousState);
    }
}
