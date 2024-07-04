package learn.mastery.data;

import learn.mastery.models.Guest;
import learn.mastery.models.Host;

import java.util.List;

public interface GuestRepository {

    List<Guest> findAll();

    Guest findByEmail(String email);
}
