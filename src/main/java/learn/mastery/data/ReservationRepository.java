package learn.mastery.data;

import learn.mastery.models.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByHost(String hostId);

    Reservation add(Reservation reservation, String hostId) throws DataException;

    boolean update(Reservation reservation, String hostId) throws DataException;

    boolean deleteById(int reservationId, String hostId) throws DataException;
}
