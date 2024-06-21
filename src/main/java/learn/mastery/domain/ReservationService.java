package learn.mastery.domain;

import learn.mastery.data.DataException;
import learn.mastery.data.ReservationRepository;
import learn.mastery.models.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public List<Reservation> findByHost(String hostId) {

        return repository.findByHost(hostId);
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
               result.addErrorMessage("Dates cannot overlap an existing reservation.");
               return result;
           }
       }

       result.setPayload(repository.add(reservation, hostId));

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

        boolean updated = repository.update(reservation, hostId);
        if (!updated) {
            result.addErrorMessage(String.format("Reservation with id: %s does not exist", reservation.getId()));
            return result;
        }

        List<Reservation> reservations = findByHost(hostId);
        for (Reservation r : reservations) {
            if (reservation.getStartDate().isBefore(r.getEndDate()) &&
                    reservation.getEndDate().isAfter(r.getStartDate())) {
                result.addErrorMessage("Dates cannot overlap an existing reservation.");
                return result;
            }
        }

        result.setPayload(reservation);
        String successMessage = String.format("Reservation %s updated", result.getPayload().getId());
        result.setSuccessMessage(successMessage);

        return result;
    }

    public Result<Reservation> deleteById(int reservationId, String hostId) throws DataException {
        Result<Reservation> result = new Result<>();
        if (!repository.deleteById(reservationId, hostId)) {
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

//        if (!result.isSuccess()) {
//            return result;
//        }

//        validateStartDate(reservation, result);

//        return result;
    }

//    private Result<Reservation> validateNulls(Reservation reservation) {
//
//        Result<Reservation> result = new Result<>();
//
//        if (reservation == null) {
//            result.addErrorMessage("Nothing to save.");
//            return result;
//        }
//
//        if (reservation.getStartDate() == null) {
//            result.addErrorMessage("Start date is required.");
//        }
//
//        if (reservation.getEndDate() == null) {
//            result.addErrorMessage("End date is required.");
//        }
//
//        return result;
//    }

//    private void validateStartDate(Reservation reservation, Result<Reservation> result) {
//        if (reservation.getStartDate().isBefore(LocalDate.now())) {
//            result.addErrorMessage("Start date cannot be in the past.");
//        }
//    }


}
