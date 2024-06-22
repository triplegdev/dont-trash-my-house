package learn.mastery.data;

import learn.mastery.models.Guest;
import learn.mastery.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    static final String SEED_FILE_PATH = "./data/reservations_test/9d469342-ad0b-4f5a-8d28-e81e690ba29a-seed.csv";
    static final String TEST_FILE_PATH = "./data/reservations_test/9d469342-ad0b-4f5a-8d28-e81e690ba29a.csv";
    static final String TEST_DIR_PATH = "./data/reservations_test";

    static final String HOST_ID = "9d469342-ad0b-4f5a-8d28-e81e690ba29a";
    static final LocalDate TODAY = LocalDate.now();

    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindByHost() {
        List<Reservation> reservations = repository.findByHost(HOST_ID);
        assertEquals(1, reservations.size());
    }

    @Test
    void shouldNotFindByHost() {
        List<Reservation> reservations = repository.findByHost("42134d94-61ce-4e53-b232-a950e9f6baf3");
        assertEquals(0, reservations.size());
    }


    @Test
    void shouldAdd() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(TODAY);
        reservation.setEndDate(TODAY.plusDays(3));
        reservation.setTotal(new BigDecimal(600));

        Guest guest = new Guest();
        guest.setId(7);
        reservation.setGuest(guest);

        reservation = repository.add(reservation, HOST_ID);

        assertEquals(2, reservation.getId());
    }

    @Test
    public void shouldUpdate() throws DataException {
        List<Reservation> reservations = repository.findByHost(HOST_ID);
        Reservation reservation = reservations.get(0);
        reservation.setEndDate(TODAY);

        boolean result = repository.update(reservation, HOST_ID);
        assertTrue(result);

        assertNotNull(reservation);

        assertEquals(1, reservation.getId());
        assertEquals(LocalDate.parse("2020-07-01"), reservation.getStartDate());
        assertEquals(TODAY, reservation.getEndDate());
        assertEquals(18, reservation.getGuest().getId());
        assertEquals(new BigDecimal(870), reservation.getTotal());
    }

    @Test
    public void shouldNotUpdateUnknownId() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(99999);
        reservation.setStartDate(TODAY);
        reservation.setEndDate(TODAY.plusDays(3));
        reservation.setTotal(new BigDecimal(600));

        Guest guest = new Guest();
        guest.setId(7);
        reservation.setGuest(guest);

        boolean result = repository.update(reservation, HOST_ID);
        assertFalse(result);
    }

    @Test
    public void shouldDelete() throws DataException {
        boolean result = repository.deleteById(1, HOST_ID);
        assertTrue(result);

        List<Reservation> reservation = repository.findByHost(HOST_ID);
        assertEquals(0, reservation.size());
    }

    @Test
    public void shouldNotDeleteUnknown() throws DataException {
        boolean result = repository.deleteById(99999, HOST_ID);
        assertFalse(result);
    }







}