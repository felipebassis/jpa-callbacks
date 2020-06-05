package br.com.iadtec.demo.listener.consumer;

import br.com.iadtec.demo.entity.auditory.Auditory;
import br.com.iadtec.demo.entity.auditory.AuditoryRepository;
import br.com.iadtec.demo.entity.auditory.AuditoryValue;
import br.com.iadtec.demo.entity.auditory.Operation;
import br.com.iadtec.demo.listener.message.AuditoryUpdateMessage;
import br.com.iadtec.demo.util.AuditoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuditoryUpdateConsumer extends AuditorySpringConsumer<AuditoryUpdateMessage> {

    protected AuditoryUpdateConsumer(EntityManager entityManager,
                                     AuditoryRepository auditoryRepository) {
        super(entityManager, auditoryRepository);
    }

    @Async
    @Transactional
    @EventListener
    @Override
    public void listenMessage(AuditoryUpdateMessage message) {
        Map<String, Object> currentState = AuditoryUtils.getEntityStateAsMap(message.getCurrentState(), entityManager);
        log.warn("{}", currentState.equals(message.getPreviousState()));
        log.info("Passou {} - {}", message.getPreviousState(), currentState);
        Auditory auditory = Auditory.builder()
                .operation(Operation.UPDATE)
                .auditoryValues(message.getPreviousState()
                        .entrySet()
                        .stream()
                        .filter(entry -> !entry.getValue().equals(currentState.get(entry.getKey())))
                        .map(entry -> new AuditoryValue(entry.getKey(), entry.getValue().toString(), currentState.getOrDefault(entry.getKey(), "null").toString()))
                        .collect(Collectors.toList()))
                .build();
        auditoryRepository.save(auditory);
    }
}
