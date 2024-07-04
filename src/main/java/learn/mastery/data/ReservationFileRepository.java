package learn.mastery.data;

import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository {

    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;

    public ReservationFileRepository(@Value("${reservationsDirectory}") String directory) {
        this.directory = directory;
    }

    // will be called in service when asking the user for host email
    @Override
    public List<Reservation> findByHost(String hostId) {
        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(hostId)))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }


    @Override
    public Reservation add(Reservation reservation, String hostId) throws DataException {
        List<Reservation> all = findByHost(hostId);
        int nextId = getNextId(all);
        reservation.setId(nextId);
        all.add(reservation);
        writeAll(all, hostId);
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation, String hostId) throws DataException {
        List<Reservation> all = findByHost(hostId);
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == reservation.getId()) {
                all.set(i, reservation);
                writeAll(all, hostId);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteById(int reservationId, String hostId) throws DataException {
        List<Reservation> all = findByHost(hostId);
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == reservationId) {
                all.remove(i);
                writeAll(all, hostId);
                return true;
            }
        }
        return false;
    }

    private int getNextId(List<Reservation> reservations) {
        int maxId = 0;
        for (Reservation reservation : reservations) {
            if (maxId < reservation.getId()) {
                maxId = reservation.getId();
            }
        }
        return maxId + 1;
    }

    private String getFilePath(String hostId) {
        return Paths.get(directory, hostId + ".csv").toString();
    }

    private void writeAll(List<Reservation> reservations, String hostId) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(hostId))) {

            writer.println(HEADER);

            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuest().getId(),
                reservation.getTotal());
    }

    private Reservation deserialize(String[] fields) {
        Reservation result = new Reservation();
        result.setId(Integer.parseInt(fields[0]));
        result.setStartDate(LocalDate.parse(fields[1]));
        result.setEndDate(LocalDate.parse(fields[2]));
        result.setTotal(new BigDecimal(fields[4]));

        Guest guest = new Guest();
        guest.setId(Integer.parseInt(fields[3]));
        result.setGuest(guest);

        return result;
    }
}
