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
import java.io.Serializable;
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

    @PostPersist
    public <ID extends Serializable> void showStateOnPersist(Auditable<ID> entity) {
        log.info("[CURRENT_STATE] - {}", entity);
    }

    @PostUpdate
    public <ID extends Serializable> void showPreviousAndCurrentState(Auditable<ID> entity) {
        log.info("[PREVIOUS_STATE] - {}; [CURRENT_STATE] - {}", entity.getPreviousState(), entity);
    }

    @PostRemove
    public <ID extends Serializable> void showStateOnDeletion(Auditable<ID> entity) {
        log.info("[CURRENT_STATE] - {}", entity);
    }
}
