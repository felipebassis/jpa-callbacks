package br.com.iadtec.demo.listener.consumer;

import br.com.iadtec.demo.entity.auditory.Auditory;
import br.com.iadtec.demo.entity.auditory.AuditoryRepository;
import br.com.iadtec.demo.entity.auditory.AuditoryValue;
import br.com.iadtec.demo.entity.auditory.Operation;
import br.com.iadtec.demo.listener.message.AuditoryDeleteMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.persistence.EntityManager;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuditoryDeleteConsumer extends AuditorySpringConsumer<AuditoryDeleteMessage> {

    protected AuditoryDeleteConsumer(EntityManager entityManager,
                                     AuditoryRepository auditoryRepository) {
        super(entityManager, auditoryRepository);
    }

    @Async
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    @Override
    public void listenMessage(AuditoryDeleteMessage message) {
        log.info("Passou {}", message.getPreviousState());
        Auditory auditory = Auditory.builder()
                .operation(Operation.DELETE)
                .auditoryValues(message.getPreviousState()
                        .entrySet()
                        .stream()
                        .map(entry -> new AuditoryValue(entry.getKey(), entry.getValue().toString()))
                        .collect(Collectors.toList()))
                .build();
        auditoryRepository.save(auditory);

    }
}
