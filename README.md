# PoC JPA Callbacks

##### Artigos relacionados
* [Hibernate Docs: Entity listeners and Callback methods](https://docs.jboss.org/hibernate/core/4.0/hem/en-US/html/listeners.html)
* [Auditing with JPA, Hibernate and Spring Data JPA](https://www.baeldung.com/database-auditing-jpa)
* [JPA Entity Lifecycle Events](https://www.baeldung.com/jpa-entity-lifecycle-events)

## Objetivo

Esta Prova de Conceito tem como objetivo validar a utilização dos Callback Methods
da JPA para adquirir os estados das entidades gerenciadas pelo JPA para razões de
auditoria de banco de dados.

## Conclusão 

Foi constatado que é possível utilizar os Callback Methods para propósitos de
auditoria, porém, alguns cuidados devem ser tomados com relação aos eventos de update 
e delete (detalhes no JavaDoc das classes 
[AuditoryJPAListener](src/main/java/br/com/iadtec/demo/listener/AuditoryJpaListener.java), [Auditable](src/main/java/br/com/iadtec/demo/entity/Auditable.java), [AuditoryJpaListenerCache](src/main/java/br/com/iadtec/demo/listener/cache/AuditoryJpaListenerCache.java)
). 

## Executando o projeto
Deve ter uma instância Postgres rodando na porta `5435` com a database `demo-jpa-callbacks` para executar o projeto. 
Caso não haja, basta rodar o [dockerfile](db.create.dockerfile) que esta na raiz do projeto com o comando 
`docker build -f db.create.dockerfile -t poc/postgres .` e em seguida rodar o comando 
`docker run -d --name poc_db -p 5435:5432 poc/postgres` para criar o container.
As tabelas serão automaticamente criadas ao subir o projeto pelo liquibase.
Há uma [collection do Postman](postman_collection.json) com todas as requests que podem ser feitas.

 


 