package learn.mastery.data;

import learn.mastery.models.Host;
import learn.mastery.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository {


    private final ArrayList<Reservation> reservations = new ArrayList<>();

    final LocalDate today = LocalDate.now();
    final LocalDate week_from_today = LocalDate.now().plusWeeks(1);

    public ReservationRepositoryDouble() {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(week_from_today);
        reservation.setEndDate(week_from_today.plusDays(3));
        reservation.setGuestId(24);
        reservation.setTotal(new BigDecimal(600));
        reservations.add(reservation);

        Reservation reservation2 = new Reservation();
        reservation2.setId(2);
        reservation2.setStartDate(today);
        reservation2.setEndDate(today.plusDays(3));
        reservation2.setGuestId(24);
        reservation2.setTotal(new BigDecimal(600));
        reservations.add(reservation2);


    }

    @Override
    public List<Reservation> findByHost(String host_id) {
        return reservations;
    }

    @Override
    public Reservation add(Reservation reservation, String host_id) throws DataException {
//        reservation.setId(2);
//        reservations.add(reservation);
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation, String host_id) throws DataException {
        return reservation.getId() > 0;
    }

    @Override
    public boolean deleteById(int reservationId, String host_id) throws DataException {
        return reservationId != 9999;
    }
}
