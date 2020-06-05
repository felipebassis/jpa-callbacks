package br.com.iadtec.demo.listener.cache;


import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Classe estática responsável por guardar em um cache o estado anterior das entidades que passarão pelo
 * {@link javax.persistence.PreUpdate} e pelo {@link javax.persistence.PostUpdate} na classe
 * {@link br.com.iadtec.demo.listener.AuditoryJpaListener}. Esse cache se faz necessário por
 * dois motivos:
 *
 * 1. O estado anterior do objeto que esta sendo persistido só pode ser adquirido se for feita
 * uma consulta no {@link javax.persistence.PreUpdate}, porém o objeto não pode ser enfileirado
 * para ser auditado se não foi feito o commit da transação, isso só pode ser validado na chamada do
 * {@link javax.persistence.PostUpdate}.
 *
 * 2. Os métodos anotados com {@link javax.persistence.PostUpdate} são chamados uma vez pra cada
 * UPDATE que foi realizado no banco com sucesso, porém como não há uma transação ativa nos métodos de
 * {@link javax.persistence.PreUpdate} e {@link javax.persistence.PostUpdate}, o listener de eventos
 * do UPDATE não pode utilizar o recurso de {@link org.springframework.transaction.event.TransactionalEventListener}
 * para apenas consumir as mensagens caso seja feito o commit da transação atual, portanto, é necessario
 * marcar as entidades em cache que foram atualizadas com sucesso no banco de dados
 * (i.e: caso tenham passado pelo {@link javax.persistence.PostUpdate} significa que a atualização
 * foi bem sucedida), e caso todas as entidades forem atualizadas com sucesso, elas devem ser enfileiradas para
 * serem auditadas.
 */
public final class AuditoryJpaListenerCache {

    /**
     * Como todas as chamadas dos Callbacks do JPA são feitas de maneira síncrona (na mesma thread),
     * não é necessario que se use classes do java.util.concurrent, apenas uma variável com escopo
     * de Thread consegue assegurar a atomicidade do Cache.
     */
    private static final ThreadLocal<AuditoryCacheMap> PREVIOUS_STATE_CACHE = new ThreadLocal<>();

    private AuditoryJpaListenerCache() {
        throw new UnsupportedOperationException();
    }

    /**
     * Adiciona uma nova entidade no cache.
     * @param entityClass classe da entidade.
     * @param id id da entidade.
     * @param previousState estado anterior representado por um mapa onde a chave é o nome da coluna
     *                      no banco de dados, e o valor é o valor da coluna no banco.
     */
    public static void put(Class<?> entityClass, Serializable id, Map<String, Object> previousState) {
        if (Objects.isNull(PREVIOUS_STATE_CACHE.get())) {
            PREVIOUS_STATE_CACHE.set(new AuditoryCacheMap());
        }
        PREVIOUS_STATE_CACHE.get()
                .put(entityClass, id,previousState);
    }

    public static Stream<AuditoryCacheMap.Value> stream() {
        return PREVIOUS_STATE_CACHE.get()
                .values()
                .stream()
                .filter(AuditoryCacheMap.Value::isNotEmpty);
    }

    public static void markAsCommitted(Class<?> entityClass, Serializable id) {
        PREVIOUS_STATE_CACHE
                .get()
                .markAsCommitted(entityClass, id);
    }

    public static boolean isAllCachedEntitiesCommitted() {
        return PREVIOUS_STATE_CACHE.get()
                .isAllEntitiesCommitted();
    }

    public static void clear() {
        PREVIOUS_STATE_CACHE.remove();
    }

}
