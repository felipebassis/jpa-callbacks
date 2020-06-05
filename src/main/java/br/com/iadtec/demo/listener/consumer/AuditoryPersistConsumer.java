package br.com.iadtec.demo.listener.consumer;

import br.com.iadtec.demo.entity.auditory.Auditory;
import br.com.iadtec.demo.entity.auditory.AuditoryRepository;
import br.com.iadtec.demo.entity.auditory.AuditoryValue;
import br.com.iadtec.demo.entity.auditory.Operation;
import br.com.iadtec.demo.listener.message.AuditoryPersistMessage;
import br.com.iadtec.demo.util.AuditoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuditoryPersistConsumer extends AuditorySpringConsumer<AuditoryPersistMessage> {

    public AuditoryPersistConsumer(EntityManager entityManager,
                                   AuditoryRepository auditoryRepository) {
        super(entityManager, auditoryRepository);
    }

    @Async
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    @Override
    public void listenMessage(AuditoryPersistMessage message) {
        Map<String, Object> entityStateAsMap = AuditoryUtils.getEntityStateAsMap(message.getCurrentState(), entityManager);
        log.info("Passou {}", entityStateAsMap);
        Auditory auditory = Auditory.builder()
                .operation(Operation.INSERT)
                .auditoryValues(entityStateAsMap.entrySet()
                        .stream()
                        .map(entry -> new AuditoryValue(entry.getKey(), entry.getValue().toString()))
                        .collect(Collectors.toList()))
                .build();
        auditoryRepository.save(auditory);
    }

}
