package uk.ac.newcastle.enterprisemiddleware.coursework.flight;

import java.io.Serializable;
import java.util.Date;

public class FlightBooking implements Serializable {
    private static final long serialVersionUID = 145672123867L;

    private Long id;
    private Long flightId;
    private Long customerId;
    private Date Date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getFlightDate() {
        return Date;
    }

    public void setFlightDate(Date Date) {
        this.Date = Date;
    }

    @Override
    public String toString() {
        return "FlightBooking{" +
                "id=" + id +
                ", flightId=" + flightId +
                ", customerId=" + customerId +
                ", Date=" + Date +
                '}';
    }
}
