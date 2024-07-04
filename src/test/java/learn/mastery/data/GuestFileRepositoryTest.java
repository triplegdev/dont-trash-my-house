package learn.mastery.data;

import learn.mastery.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    //guest is read-only for now but will set up seed for future write features
    static final String SEED_FILE_PATH = "./data/guests-seed.csv";
    static final String TEST_FILE_PATH = "./data/guests-test.csv";

    static final int GUEST_COUNT = 1000;

    GuestFileRepository repository = new GuestFileRepository(TEST_FILE_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        List<Guest> guests = repository.findAll();
        assertEquals(GUEST_COUNT, guests.size());
    }

    @Test
    void shouldFindByEmail() {
        Guest guest = repository.findByEmail("slomas0@mediafire.com");
        assertEquals("Sullivan", guest.getFirstName());
        assertEquals("Lomas", guest.getLastName());
        assertEquals("slomas0@mediafire.com", guest.getEmail());
        assertEquals("(702) 7768761", guest.getPhone());
        assertEquals("NV", guest.getState());
    }

    @Test
    void shouldNotFindByEmail() {
        Guest guest = repository.findByEmail("fake@email.com");
        assertNull(guest);
    }

}