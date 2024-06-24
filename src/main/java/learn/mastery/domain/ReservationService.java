package learn.mastery.domain;

import learn.mastery.data.DataException;
import learn.mastery.data.GuestRepository;
import learn.mastery.data.ReservationRepository;
import learn.mastery.models.Guest;
import learn.mastery.models.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;

    public ReservationService(ReservationRepository reservationRepository, GuestRepository guestRepository) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
    }

    public List<Reservation> findByHost(String hostId) {

        Map<String, Guest> guestMap = guestRepository.findAll().stream()
                .collect(Collectors.toMap(i -> String.valueOf(i.getId()), i -> i));

        List<Reservation> reservations = reservationRepository.findByHost(hostId);
        for (Reservation reservation : reservations) {
            reservation.setGuest(guestMap.get(String.valueOf(reservation.getGuest().getId())));
        }

        return reservations;
    }

    public Result<Reservation> add(Reservation reservation, String hostId) throws DataException {

       Result<Reservation> result = validate(reservation);
       if (!result.isSuccess()) {
           return result;
       }

       List<Reservation> reservations = findByHost(hostId);
       for (Reservation r : reservations) {
           if (reservation.getStartDate().isBefore(r.getEndDate()) &&
                   reservation.getEndDate().isAfter(r.getStartDate())) {
               result.addErrorMessage("Dates conflict with an existing reservation.");
               return result;
           }
       }

       result.setPayload(reservationRepository.add(reservation, hostId));

        if (result.isSuccess()) {
            String successMessage = String.format("Reservation %s added.", result.getPayload().getId());
            result.setSuccessMessage(successMessage);
        }

       return result;
    }

    public Result<Reservation> update(Reservation reservation, String hostId) throws DataException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        List<Reservation> reservations = findByHost(hostId);
        for (Reservation r : reservations) {
            if (r.getId() != reservation.getId()) { // Check if it's the same reservation
                if (reservation.getStartDate().isBefore(r.getEndDate()) &&
                        reservation.getEndDate().isAfter(r.getStartDate())) {
                    result.addErrorMessage("Dates conflict with an existing reservation.");
                    return result;
                }
            }
        }

        boolean updated = reservationRepository.update(reservation, hostId);
        if (!updated) {
            result.addErrorMessage(String.format("Reservation with id: %s does not exist", reservation.getId()));
            return result;
        }

        result.setPayload(reservation);
        String successMessage = String.format("Reservation %s updated", result.getPayload().getId());
        result.setSuccessMessage(successMessage);

        return result;
    }

    public Result<Reservation> deleteById(int reservationId, String hostId) throws DataException {
        Result<Reservation> result = new Result<>();
        if (!reservationRepository.deleteById(reservationId, hostId)) {
            result.addErrorMessage(String.format("Reservation with id: %s does not exist", reservationId));
        }
        if (result.isSuccess()) {
            String successMessage = String.format("Reservation %s removed.", reservationId);
            result.setSuccessMessage(successMessage);
        }
        return result;
    }


    private Result<Reservation> validate(Reservation reservation) {

        Result<Reservation> result = new Result<>();

        if (reservation == null) {
            result.addErrorMessage("Nothing to save.");
            return result;
        }

        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Start date is required.");
        }

        if (reservation.getEndDate() == null) {
            result.addErrorMessage("End date is required.");
        }


        return result;
    }

    public List<Reservation> sortByDate(List<Reservation> reservations) {
        return reservations.stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .toList();
    }

    public List<Reservation> getGuestReservations(List<Reservation> reservations, int guestId) {
        return reservations.stream()
                .filter(r -> r.getGuest().getId() == guestId)
                .toList();
    }

    public List<Reservation> getFutureGuestReservations(List<Reservation> reservations, int guestId) {
        return reservations.stream()
                .filter(r -> r.getGuest().getId() == guestId && r.getStartDate().isAfter(LocalDate.now()))
                .toList();
    }



}
