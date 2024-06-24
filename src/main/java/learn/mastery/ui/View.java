package learn.mastery.ui;

import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class View {

    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }


    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            if (!option.isHidden()) {
                io.printf("%s. %s%n", option.getValue(), option.getMessage());
            }
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

//    public boolean checkDates(List<Reservation> reservations, Reservation reservation) {
//        for (Reservation r : reservations) {
//            if (r.getId() != reservation.getId()) {
//                if (reservation.getStartDate().isBefore(r.getEndDate()) &&
//                        reservation.getEndDate().isAfter(r.getStartDate())) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }

    public Reservation chooseReservation(List<Reservation> reservations) {
        Reservation reservation;
        do {
            int reservationId = io.readInt("Reservation ID: ");

             reservation = reservations.stream()
                    .filter(r -> r.getId() == reservationId)
                    .findFirst()
                    .orElse(null);
            if (reservation == null) {
                displayStatus(false, "Reservation " + reservationId + " not found.");
            }
            System.out.println();
        } while (reservation == null);

        return reservation;

    }

    public Reservation update(Reservation reservation, Host host) {
        reservation.setStartDate(Optional.ofNullable(readUpdatedStartDate("Start (MM/dd/yyyy): "))
                                         .orElse(reservation.getStartDate()));
        reservation.setEndDate(Optional.ofNullable(readUpdatedEndDate("End (MM/dd/yyyy): ", reservation.getStartDate()))
                                       .orElse(reservation.getEndDate()));

        BigDecimal newTotal = reservation.calculateTotal(host.getStandardRate(), host.getWeekendRate());
        reservation.setTotal(newTotal);

        return reservation;
    }

    public LocalDate readUpdatedStartDate(String prompt) {
        LocalDate startDate;
        do {
            startDate = io.readDate(prompt);
            if (startDate == null) {
                return startDate;
            }
            if (startDate.isBefore(LocalDate.now())) {
                displayStatus(false, "Start date must not be in the past.");
                System.out.println();
            }
        } while (startDate.isBefore(LocalDate.now()));

        return startDate;
    }

    public LocalDate readUpdatedEndDate(String prompt, LocalDate startDate) {
        LocalDate endDate;
        do {
            endDate = io.readDate(prompt);
            if (endDate == null) {
                return endDate;
            }
            if (endDate.isBefore(startDate)) {
                displayStatus(false, "End date must come after start date.");
                System.out.println();
            }
        } while (endDate.isBefore(startDate));

        return endDate;
    }

    public LocalDate readStartDate(String prompt) {
        LocalDate startDate;
        do {
            startDate = io.readLocalDate(prompt);
            if (startDate.isBefore(LocalDate.now())) {
                displayStatus(false, "Start date must not be in the past.");
                System.out.println();
            }
        } while (startDate.isBefore(LocalDate.now()));

        return startDate;
    }

    public LocalDate readEndDate(String prompt, LocalDate startDate) {
        LocalDate endDate;
        do {
            endDate = io.readLocalDate(prompt);
            if (endDate.isBefore(startDate)) {
                displayStatus(false, "End date must come after start date.");
                System.out.println();
            }
        } while (endDate.isBefore(startDate));

        return endDate;
    }

    public Reservation makeReservation(Guest guest, Host host) {
        Reservation reservation = new Reservation();
        reservation.setStartDate(readStartDate("Start (MM/dd/yyyy): "));
        reservation.setEndDate(readEndDate("End (MM/dd/yyyy): ", reservation.getStartDate()));
        reservation.setGuest(guest);
        reservation.setTotal(reservation.calculateTotal(host.getStandardRate(), host.getWeekendRate()));

        return reservation;
    }

    public String makeSummary(Reservation reservation) {
        displayHeader("Summary");
        io.println("Start: " + reservation.getStartDate());
        io.println("End: " + reservation.getEndDate());
        io.println("Total: $" + reservation.getTotal());
        return io.readRequiredString("Is this okay? [y/n]: ");
    }

    public String getHostEmail() { return io.readRequiredString("Host Email: "); }

    public String getGuestEmail() { return io.readRequiredString("Guest Email: "); }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    public String confirmDelete() {
        displayHeader("Confirm Cancellation");
        return io.readRequiredString("Are you sure? [y/n]: ");
    }

    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    public void displayHostError() {
        displayHeader("Error");
        io.println("Sorry, host does not exist.");
    }

    public void displayGuestError() {
        displayHeader("Error");
        io.println("Sorry, guest does not exist.");
    }

    public void displayReservations(List<Reservation> reservations, Host host) {
        if (reservations.isEmpty()) {
            io.println("No reservations were found.");
            return;
        }

        displayHeader(String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState()));

        for (Reservation reservation : reservations) {
            io.printf("ID: %s, %s - %s, Guest: %s, %s, Email: %s%n",
                    reservation.getId(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    reservation.getGuest().getLastName(),
                    reservation.getGuest().getFirstName(),
                    reservation.getGuest().getEmail()
            );
        }
    }


}
