package br.com.iadtec.demo.integration;

import br.com.iadtec.demo.dto.CarBrandDTO;
import br.com.iadtec.demo.entity.auditory.Auditory;
import br.com.iadtec.demo.entity.auditory.AuditoryRepository;
import br.com.iadtec.demo.entity.auditory.AuditoryValue;
import br.com.iadtec.demo.persistence.CarBrandRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.net.URI;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Sql(statements = "delete from car_brand;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class IntegrationTest {

    private final String url = "http://localhost:8081";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarBrandRepository carBrandRepository;

    @Autowired
    private AuditoryRepository auditoryRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        auditoryRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        auditoryRepository.deleteAll();
    }

    @Test
    void shouldPersistEntityAndAuditory() throws InterruptedException {
        CarBrandDTO carBrandDTO = new CarBrandDTO();
        carBrandDTO.setName("TestIntegracao1Test");
        ResponseEntity<Long> request = restTemplate.exchange(url.concat("/carBrand"),
                HttpMethod.POST,
                new HttpEntity<>(carBrandDTO),
                Long.class);
        assertNotNull(request.getBody());

        Thread.sleep(200);

        List<Auditory> auditories = auditoryRepository.findAll();

        assertEquals(1, auditories.size());
        Auditory auditory = auditories.get(0);

        List<AuditoryValue> auditoryValues = auditory.getAuditoryValues();
        assertEquals(2, auditoryValues.size());

        auditoryValues.sort(Comparator.comparing(AuditoryValue::getColumnName));
        AuditoryValue auditoryValue = auditoryValues.get(0);

        assertEquals("id", auditoryValue.getColumnName());
        assertNull(ReflectionTestUtils.getField(auditoryValue, "previousValue"));
        assertEquals(request.getBody().toString(), ReflectionTestUtils.getField(auditoryValue, "currentValue").toString());

        auditoryValue = auditoryValues.get(1);

        assertEquals("name", auditoryValue.getColumnName());
        assertNull(ReflectionTestUtils.getField(auditoryValue, "previousValue"));
        assertEquals("TestIntegracao1Test", ReflectionTestUtils.getField(auditoryValue, "currentValue"));
    }

    @Sql(statements = "insert into car_brand (id, name) values (nextval('car_brand_id_seq'), 'TestIntegracao1');",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void shouldUpdateExistingEntityAndPersistAuditory() throws InterruptedException {
        long carBrandId = ((BigInteger) entityManager.createNativeQuery("select currval('car_brand_id_seq')")
                .getSingleResult())
                .longValue();

        CarBrandDTO carBrandDTO = new CarBrandDTO();
        carBrandDTO.setName("TestIntegracao");
        restTemplate.exchange(url.concat("/carBrand/").concat(Long.toString(carBrandId)),
                HttpMethod.PUT,
                new HttpEntity<>(carBrandDTO),
                Void.class);

        Thread.sleep(1000L);

        List<Auditory> auditories = auditoryRepository.findAll();

        assertEquals(1, auditories.size());
        Auditory auditory = auditories.get(0);

        List<AuditoryValue> auditoryValues = auditory.getAuditoryValues();
        assertEquals(1, auditoryValues.size());

        auditoryValues.sort(Comparator.comparing(AuditoryValue::getColumnName));
        AuditoryValue auditoryValue = auditoryValues.get(0);

        assertEquals("name", auditoryValue.getColumnName());
        assertEquals("TestIntegracao1", ReflectionTestUtils.getField(auditoryValue, "previousValue"));
        assertEquals("TestIntegracao", ReflectionTestUtils.getField(auditoryValue, "currentValue"));
    }

    @Sql(statements = "insert into car_brand (id, name) values (nextval('car_brand_id_seq'), 'TestIntegracao1');",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void shouldDeleteEntityAndPersistAuditory() throws InterruptedException {
        long carBrandId = ((BigInteger) entityManager.createNativeQuery("select currval('car_brand_id_seq')")
                .getSingleResult())
                .longValue();

        CarBrandDTO carBrandDTO = new CarBrandDTO();
        carBrandDTO.setName("TestIntegracao");
        restTemplate.exchange(new RequestEntity<>(HttpMethod.DELETE,
                URI.create(url.concat("/carBrand/").concat(Long.toString(carBrandId)))), Void.class);

        Thread.sleep(200L);

        List<Auditory> auditories = auditoryRepository.findAll();

        assertEquals(1, auditories.size());
        Auditory auditory = auditories.get(0);

        List<AuditoryValue> auditoryValues = auditory.getAuditoryValues();
        assertEquals(2, auditoryValues.size());

        auditoryValues.sort(Comparator.comparing(AuditoryValue::getColumnName));
        AuditoryValue auditoryValue = auditoryValues.get(0);

        assertEquals("id", auditoryValue.getColumnName());
        assertNull(ReflectionTestUtils.getField(auditoryValue, "previousValue"));
        assertEquals(Long.toString(carBrandId), ReflectionTestUtils.getField(auditoryValue, "currentValue").toString());

        auditoryValue = auditoryValues.get(1);

        assertEquals("name", auditoryValue.getColumnName());
        assertNull(ReflectionTestUtils.getField(auditoryValue, "previousValue"));
        assertEquals("TestIntegracao1", ReflectionTestUtils.getField(auditoryValue, "currentValue"));
    }

    @Sql(statements = "insert into car_brand (id, name) values (nextval('car_brand_id_seq'), 'TestIntegracao1');" +
            "insert into car_brand(id, name) values (nextval('car_brand_id_seq'), 'Test')",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void shouldNotPersistAuditoryWhenUpdateFailed() throws InterruptedException {
        long carBrandId = ((BigInteger) entityManager.createNativeQuery("select currval('car_brand_id_seq')")
                .getSingleResult())
                .longValue();

        CarBrandDTO carBrandDTO = new CarBrandDTO();
        carBrandDTO.setName("TestIntegracao1");
        restTemplate.exchange(url.concat("/carBrand/").concat(Long.toString(carBrandId)),
                HttpMethod.PUT,
                new HttpEntity<>(carBrandDTO),
                Void.class);

        Thread.sleep(2000L);

        List<Auditory> auditories = auditoryRepository.findAll();

        assertEquals(0, auditories.size());
    }
}
