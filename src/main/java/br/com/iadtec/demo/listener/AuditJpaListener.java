package br.com.iadtec.demo.listener;

import br.com.iadtec.demo.entity.Auditable;
import br.com.iadtec.demo.util.Autowirer;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.FlushMode;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.orm.jpa.EntityManagerFactoryUtils.closeEntityManager;

@Slf4j
@NoArgsConstructor
@Component
public class AuditJpaListener {

    private static final Map<Key, Auditable<?>> PREVIOUS_STATE = new ConcurrentHashMap<>();

    @PrePersist
    @PreUpdate
    public <ID> void storeEntityState(Auditable<ID> entity) {
        Optional<Auditable<?>> auditableOp = Optional.ofNullable(entity.getPreviousState());
        auditableOp.ifPresent(auditable -> {
            PREVIOUS_STATE.put(Key.from(entity.getClass().getSimpleName(), entity.getId()), auditable);
            log.info("[CURRENT_STATE_STORED] - {}", auditable);
        });
    }

    @PostPersist
    @PostUpdate
    public <ID> void showPreviousAndCurrentState(Auditable<ID> entity) {
        Key key = Key.from(entity.getClass().getSimpleName(), entity.getId());
        Auditable<?> previousState = PREVIOUS_STATE.get(key);

        log.info("[PREVIOUS_STATE] - {}; [CURRENT_STATE] - {}", previousState, entity);

        PREVIOUS_STATE.remove(key);
    }

    @EqualsAndHashCode
    private static class Key {
        private String className;
        private Object id;

        private static Key from(String className, Object id) {
            Key key = new Key();
            key.className = className;
            key.id = id;
            return key;
        }
    }
}
