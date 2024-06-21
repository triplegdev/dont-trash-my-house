package learn.mastery.ui;

import learn.mastery.data.DataException;
import learn.mastery.domain.GuestService;
import learn.mastery.domain.HostService;
import learn.mastery.domain.ReservationService;
import org.springframework.stereotype.Component;

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
        view.displayHeader("Welcome to Sustainable Foraging");
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
                    System.out.println("view reservations by host");
                    break;
                case ADD_RESERVATION:
                    System.out.println("add reservation");
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
}
