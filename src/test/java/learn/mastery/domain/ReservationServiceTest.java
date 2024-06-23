package learn.mastery.domain;

import learn.mastery.data.DataException;
import learn.mastery.data.GuestRepositoryDouble;
import learn.mastery.data.ReservationRepositoryDouble;
import learn.mastery.models.Guest;
import learn.mastery.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service;

    static final String HOST_ID = "9d469342-ad0b-4f5a-8d28-e81e690ba29a";
    static final LocalDate TODAY = LocalDate.now();

    @BeforeEach
    void setup() {
        ReservationRepositoryDouble repository = new ReservationRepositoryDouble();
        service = new ReservationService(repository, new GuestRepositoryDouble());
    }

    @Test
    void shouldFindByHost() {
        List<Reservation> reservations = service.findByHost(HOST_ID);
        assertEquals(2, reservations.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(3);
        reservation.setStartDate(TODAY.plusDays(4));
        reservation.setEndDate(TODAY.plusDays(6));

        Guest guest = new Guest();
        guest.setId(14);
        reservation.setGuest(guest);

        reservation.setTotal(new BigDecimal(750));

        Result<Reservation> expected = new Result<>();
        expected.setPayload(reservation);

        Result<Reservation> actual = service.add(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldNotAddNull() throws DataException {
        Result<Reservation> expected = makeResult("Nothing to save.");
        Result<Reservation> actual = service.add(null, HOST_ID);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddNullStartDate() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        reservation.setStartDate(null);
        reservation.setEndDate(TODAY.plusDays(3));

        Guest guest = new Guest();
        guest.setId(14);
        reservation.setGuest(guest);

        reservation.setTotal(new BigDecimal(750));

        Result<Reservation> expected = makeResult("Start date is required.");
        Result<Reservation> actual = service.add(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldNotAddNullEndDate() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        reservation.setStartDate(TODAY);
        reservation.setEndDate(null);

        Guest guest = new Guest();
        guest.setId(14);
        reservation.setGuest(guest);

        reservation.setTotal(new BigDecimal(750));

        Result<Reservation> expected = makeResult("End date is required.");
        Result<Reservation> actual = service.add(reservation, HOST_ID);
        assertEquals(expected, actual);

    }


    @Test
    void shouldNotAddOverlappingStartDate() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        reservation.setStartDate(TODAY.plusDays(8));
        reservation.setEndDate(TODAY.plusDays(12));

        Guest guest = new Guest();
        guest.setId(14);
        reservation.setGuest(guest);

        reservation.setTotal(new BigDecimal(750));

        Result<Reservation> expected = makeResult("Dates conflict with an existing reservation.");
        Result<Reservation> actual = service.add(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldNotAddOverlappingEndDate() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        reservation.setStartDate(TODAY);
        reservation.setEndDate(TODAY.plusDays(8));

        Guest guest = new Guest();
        guest.setId(14);
        reservation.setGuest(guest);

        reservation.setTotal(new BigDecimal(750));

        Result<Reservation> expected = makeResult("Dates conflict with an existing reservation.");
        Result<Reservation> actual = service.add(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldNotAddStartDatesBetweenExistingDates() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        reservation.setStartDate(TODAY.plusDays(8));
        reservation.setEndDate(TODAY.plusDays(9));

        Guest guest = new Guest();
        guest.setId(14);
        reservation.setGuest(guest);

        reservation.setTotal(new BigDecimal(750));

        Result<Reservation> expected = makeResult("Dates conflict with an existing reservation.");
        Result<Reservation> actual = service.add(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldNotAddDatesSurroundingExistingDates() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        reservation.setStartDate(TODAY.plusDays(6));
        reservation.setEndDate(TODAY.plusDays(12));

        Guest guest = new Guest();
        guest.setId(14);
        reservation.setGuest(guest);

        reservation.setTotal(new BigDecimal(750));

        Result<Reservation> expected = makeResult("Dates conflict with an existing reservation.");
        Result<Reservation> actual = service.add(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldNotAddSameDatesAsExisting() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        reservation.setStartDate(TODAY.plusDays(7));
        reservation.setEndDate(TODAY.plusDays(10));

        Guest guest = new Guest();
        guest.setId(14);
        reservation.setGuest(guest);

        reservation.setTotal(new BigDecimal(750));

        Result<Reservation> expected = makeResult("Dates conflict with an existing reservation.");
        Result<Reservation> actual = service.add(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldUpdate() throws DataException {
        Reservation reservation = service.findByHost(HOST_ID).get(0);
        reservation.setStartDate(TODAY.plusDays(8));
        Result<Reservation> expected = new Result<>();
        expected.setPayload(reservation);

        Result<Reservation> actual = service.update(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldNotUpdateNull() throws DataException {
        Result<Reservation> expected = makeResult("Nothing to save.");
        Result<Reservation> actual = service.update(null, HOST_ID);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateNullStartDate() throws DataException {
        Reservation reservation = service.findByHost(HOST_ID).get(0);
        reservation.setStartDate(null);

        Result<Reservation> expected = makeResult("Start date is required.");
        Result<Reservation> actual = service.update(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldNotUpdateNullEndDate() throws DataException {
        Reservation reservation = service.findByHost(HOST_ID).get(0);
        reservation.setEndDate(null);

        Result<Reservation> expected = makeResult("End date is required.");
        Result<Reservation> actual = service.update(reservation, HOST_ID);
        assertEquals(expected, actual);

    }


    @Test
    void shouldNotUpdateOverlappingStartDate() throws DataException {
        Reservation reservation = service.findByHost(HOST_ID).get(0);
        reservation.setStartDate(TODAY.plusDays(1));

        Result<Reservation> expected = makeResult("Dates conflict with an existing reservation.");
        Result<Reservation> actual = service.update(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldNotUpdateOverlappingEndDate() throws DataException {
        Reservation reservation = service.findByHost(HOST_ID).get(1);
        reservation.setEndDate(TODAY.plusDays(8));

        Result<Reservation> expected = makeResult("Dates conflict with an existing reservation.");
        Result<Reservation> actual = service.update(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldNotUpdateStartDatesBetweenExistingDates() throws DataException {
        Reservation reservation = service.findByHost(HOST_ID).get(0);
        reservation.setStartDate(TODAY.plusDays(1));
        reservation.setEndDate(TODAY.plusDays(2));

        Result<Reservation> expected = makeResult("Dates conflict with an existing reservation.");
        Result<Reservation> actual = service.update(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldNotUpdateDatesSurroundingExistingDates() throws DataException {
        Reservation reservation = service.findByHost(HOST_ID).get(1);
        reservation.setStartDate(TODAY.plusDays(6));
        reservation.setEndDate(TODAY.plusDays(12));

        Result<Reservation> expected = makeResult("Dates conflict with an existing reservation.");
        Result<Reservation> actual = service.update(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldNotUpdateSameDatesAsExisting() throws DataException {
        Reservation reservation = service.findByHost(HOST_ID).get(0);
        reservation.setStartDate(TODAY);
        reservation.setEndDate(TODAY.plusDays(3));

        Result<Reservation> expected = makeResult("Dates conflict with an existing reservation.");
        Result<Reservation> actual = service.update(reservation, HOST_ID);
        assertEquals(expected, actual);

    }

    @Test
    void shouldDeleteExistingPanel() throws DataException {
        Result<Reservation> actual = service.deleteById(1, HOST_ID);
        assertTrue(actual.isSuccess());
    }

    @Test
    void shouldNotDeleteNonExistingPanel() throws DataException {
        Result<Reservation> actual = service.deleteById(9999, HOST_ID);
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getErrorMessages().size());
        assertTrue(actual.getErrorMessages().get(0).contains("does not exist"));
    }

    private Result<Reservation> makeResult(String message) {
        Result<Reservation> result = new Result<>();
        result.addErrorMessage(message);
        return result;
    }




}