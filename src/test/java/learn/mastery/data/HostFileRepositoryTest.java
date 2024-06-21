package learn.mastery.data;

import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    static final String SEED_FILE_PATH = "./data/hosts-seed.csv";
    static final String TEST_FILE_PATH = "./data/hosts-test.csv";

    static final int HOST_COUNT = 1000;

    HostFileRepository repository = new HostFileRepository(TEST_FILE_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        List<Host> hosts = repository.findAll();
        assertEquals(HOST_COUNT, hosts.size());
    }

    @Test
    void shouldFindByEmail() {
        Host host = repository.findByEmail("eyearnes0@sfgate.com");
        assertEquals("3edda6bc-ab95-49a8-8962-d50b53f84b15", host.getId());
        assertEquals("Yearnes", host.getLastName());
        assertEquals("eyearnes0@sfgate.com", host.getEmail());
        assertEquals("(806) 1783815", host.getPhone());
        assertEquals("3 Nova Trail", host.getAddress());
        assertEquals("Amarillo", host.getCity());
        assertEquals("TX", host.getState());
        assertEquals(new BigDecimal(340), host.getStandardRate());
        assertEquals(new BigDecimal(425), host.getWeekendRate());
    }

    @Test
    void shouldNotFindByEmail() {
        Host host = repository.findByEmail("fakeemail0@aol.com");
        assertNull(host);
    }

}