package learn.mastery.data;

import learn.mastery.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {

    private final static Guest GUEST = makeGuest();

    private final ArrayList<Guest> guests = new ArrayList<>();

    public GuestRepositoryDouble() { guests.add(GUEST); }

    @Override
    public List<Guest> findAll() {
        return guests;
    }

    @Override
    public Guest findByEmail(String email) {
        return findAll().stream()
                .filter(i -> i.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    private static Guest makeGuest() {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Tony");
        guest.setLastName("Stark");
        guest.setEmail("tony@starkindustries.com");
        guest.setPhone("(421) 8052746");
        guest.setState("NY");
        return guest;
    }
}
