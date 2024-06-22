package learn.mastery.ui;

import learn.mastery.data.DataException;
import learn.mastery.domain.GuestService;
import learn.mastery.domain.HostService;
import learn.mastery.domain.ReservationService;
import learn.mastery.domain.Result;
import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class Controller {
    private final HostService hostService;
    private final GuestService guestService;
    private final ReservationService reservationService;
    private final View view;

    public Controller(HostService hostService, GuestService guestService, ReservationService reservationService, View view) {
        this.hostService = hostService;
        this.guestService = guestService;
        this.reservationService = reservationService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to Don't Wreck My House");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS_FOR_HOST:
                    viewReservationsByHost();
                    break;
                case ADD_RESERVATION:
                    addReservation();
                    break;
                case UPDATE_RESERVATION:
                    System.out.println("update reservation");
                    break;
                case DELETE_RESERVATION:
                    System.out.println("delete reservation");
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    private void viewReservationsByHost() {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_FOR_HOST.getMessage());

        String hostEmail = view.getHostEmail();
        Host host = hostService.findByEmail(hostEmail);
        if (host == null) {
            view.displayHostError();
            return;
        }

        List<Reservation> reservations = reservationService.findByHost(host.getId());

        view.displayReservations(reservations, host);
        view.enterToContinue();
    }

    private void addReservation() throws DataException {
        view.displayHeader(MainMenuOption.ADD_RESERVATION.getMessage());

        String hostEmail = view.getHostEmail();
        Host host = hostService.findByEmail(hostEmail);
        if (host == null) {
            view.displayHostError();
            return;
        }

        String guestEmail = view.getGuestEmail();
        Guest guest = guestService.findByEmail(guestEmail);
        if (guest == null) {
            view.displayGuestError();
            return;
        }

        List<Reservation> reservations = reservationService.findByHost(host.getId());
        view.displayReservations(reservations, host);
//        view.displayGuestReservations(reservations, host, guest.getId());

        Reservation reservation = view.makeReservation(guest, host);
//        if (view.checkDates(reservations, reservation)) {
//            view.displayStatus(false, "Dates conflict with an existing reservation.");
//            return;
//        }
        String confirmation = view.makeSummary(reservation);

        if (confirmation.equalsIgnoreCase("y")) {
            Result<Reservation> result = reservationService.add(reservation, host.getId());
            if (!result.isSuccess()) {
                view.displayStatus(false, result.getErrorMessages());
            } else {
                String successMessage = String.format("Reservation %s created.", result.getPayload().getId());
                view.displayStatus(true, successMessage);
            }
        }

    }
}
