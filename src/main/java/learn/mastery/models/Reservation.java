package learn.mastery.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal total;
    private int guestId;
//    private Host host;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

//    public Host getHost() {
//        return host;
//    }
//
//    public void setHost(Host host) {
//        this.host = host;
//    }


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Reservation that = (Reservation) o;
//        return id == that.id && guestId == that.guestId && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(total, that.total);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, startDate, endDate, total, guestId);
//    }
}
