package uk.ac.newcastle.enterprisemiddleware.coursework.flight;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Customer;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @description 航班预订
 */

public class FlightBooking implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long customerId;


    private Long flightId;


    private Date bookingDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FlightBooking that = (FlightBooking) o;

        if (!Objects.equals(flightId, that.flightId))
            return false;
        return Objects.equals(bookingDate, that.bookingDate);
    }

    @Override
    public int hashCode() {
        int result = flightId != null ? flightId.hashCode() : 0;
        result = 31 * result + (bookingDate != null ? bookingDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FlightBooking{" +
                "customerId=" + customerId +
                ", flightId=" + flightId +
                ", bookingDate=" + bookingDate +
                '}';
    }
}
