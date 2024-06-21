package learn.mastery.domain;

import learn.mastery.data.GuestRepositoryDouble;
import learn.mastery.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    GuestService service;

    @BeforeEach
    void setup() {
        GuestRepositoryDouble repository = new GuestRepositoryDouble();
        service = new GuestService(repository);
    }

    @Test
    void shouldFindAll() {
        List<Guest> guests = service.findAll();
        assertEquals(1, guests.size());
    }

    @Test
    void shouldFindById() {
        Guest guest = service.findById(1);
        assertEquals("Tony", guest.getFirstName());
        assertEquals("Stark", guest.getLastName());
        assertEquals("tony@starkindustries.com", guest.getEmail());
        assertEquals("(421) 8052746", guest.getPhone());
        assertEquals("NY", guest.getState());
    }

    @Test
    void shouldNotFindById() {
        Guest guest = service.findById(9999);
        assertNull(guest);
    }

}