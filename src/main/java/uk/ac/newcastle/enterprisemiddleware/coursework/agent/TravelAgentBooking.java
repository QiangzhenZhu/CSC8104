package uk.ac.newcastle.enterprisemiddleware.coursework.agent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Classname TravelAgentBooking
 * @Description TODO
 * @Date 2022/11/21 20:47
 * @Created by 10835
 */
@Entity
@NamedQueries({
        @NamedQuery(name = TravelAgentBooking.FIND_TRAVEL_AGENT_BOOKING_ALL, query = "SELECT t FROM TravelAgentBooking t ORDER BY t.customerId"),
        @NamedQuery(name = TravelAgentBooking.FIND_TRAVEL_AGENT_BOOKING_BY_CUSTOMER, query = "SELECT t FROM TravelAgentBooking t WHERE t.customerId = :customerId")

})
@XmlRootElement
@Table(name = "Travel_Agent_Booking")
public class TravelAgentBooking implements Serializable {

    private static final long serialVersionUID = -6776747175287440952L;
    public static final String FIND_TRAVEL_AGENT_BOOKING_ALL = "TravelAgentBooking.findAll";
    public static final String FIND_TRAVEL_AGENT_BOOKING_BY_CUSTOMER="TravelBooking.findByCustomer";

    @Id
    @Column(name="travel_agent_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelAgentId;
    @NotNull
    @Column(name = "customer_id")
    private Long customerId;
    @NotNull
    @Column(name = "hotel_booking_id")
    private Long hotelBookingId;
    @NotNull
    @Column(name = "flight_booking_id")
    private Long flightBookingId;
    @NotNull
    @Column(name = "taxi_booking_id")
    private Long taxiBookingId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getTravelAgentId() {
        return travelAgentId;
    }

    public void setTravelAgentId(Long travelAgentId) {
        this.travelAgentId = travelAgentId;
    }

    public Long getHotelBookingId() {
        return hotelBookingId;
    }

    public void setHotelBookingId(Long hotelBookingId) {
        this.hotelBookingId = hotelBookingId;
    }

    public Long getFlightBookingId() {
        return flightBookingId;
    }

    public void setFlightBookingId(Long flightBookingId) {
        this.flightBookingId = flightBookingId;
    }

    public Long getTaxiBookingId() {
        return taxiBookingId;
    }

    public void setTaxiBookingId(Long taxiBookingId) {
        this.taxiBookingId = taxiBookingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TravelAgentBooking)) return false;
        TravelAgentBooking that = (TravelAgentBooking) o;
        return Objects.equals(getCustomerId(), that.getCustomerId()) && Objects.equals(getHotelBookingId(), that.getHotelBookingId()) && Objects.equals(getFlightBookingId(), that.getFlightBookingId()) && Objects.equals(getTaxiBookingId(), that.getTaxiBookingId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerId(), getHotelBookingId(), getFlightBookingId(), getTaxiBookingId());
    }

    @Override
    public String toString() {
        return "TravelAgentBooking{" +
                "customerId=" + customerId +
                ", hotelBookingId=" + hotelBookingId +
                ", flightBookingId=" + flightBookingId +
                ", taxiBookingId=" + taxiBookingId +
                '}';
    }
}
