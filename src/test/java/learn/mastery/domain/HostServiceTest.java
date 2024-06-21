package learn.mastery.domain;

import learn.mastery.data.HostRepositoryDouble;
import learn.mastery.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {

    HostService service;

    @BeforeEach
    void setup() {
        HostRepositoryDouble repository = new HostRepositoryDouble();
        service = new HostService(repository);
    }

    @Test
    void shouldFindAll() {
        List<Host> hosts = service.findAll();
        assertEquals(1, hosts.size());
    }

    @Test
    void shouldFindByEmail() {
        Host host = service.findByEmail("smith@example.com");
        assertEquals("5a7bca2e-9e4d-4346-b719-f0f7c1f8e43b", host.getId());
        assertEquals("Smith", host.getLastName());
        assertEquals("smith@example.com", host.getEmail());
        assertEquals("(123) 4567890", host.getPhone());
        assertEquals("123 Main St", host.getAddress());
        assertEquals("Los Angeles", host.getCity());
        assertEquals("CA", host.getState());
        assertEquals("12345", host.getPostalCode());
        assertEquals(new BigDecimal(150), host.getStandardRate());
        assertEquals(new BigDecimal(175), host.getWeekendRate());
    }

    @Test
    void shouldNotFindByEmail() {
        Host host = service.findByEmail("fakeemail0@aol.com");
        assertNull(host);
    }

}