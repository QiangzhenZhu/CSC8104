package uk.ac.newcastle.enterprisemiddleware.coursework.agent;

import uk.ac.newcastle.enterprisemiddleware.coursework.flight.FlightBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.GuestBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.HotelBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.taxi.TaxiBooking;

import java.io.Serializable;

/**
 * @Classname TravelAgent
 * @Description TODO
 * @Date 2022/11/21 20:42
 * @Created by 10835
 */

public class TravelAgent implements Serializable {
    private static final long serialVersionUID = 3057427517334448909L;

    private FlightBooking flightBooking;

    private HotelBooking hotelBooking;

    private TaxiBooking taxiBooking;

    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public FlightBooking getFlightBooking() {
        return flightBooking;
    }

    public void setFlightBooking(FlightBooking flightBooking) {
        this.flightBooking = flightBooking;
    }

    public HotelBooking getHotelBooking() {
        return hotelBooking;
    }

    public void setHotelBooking(HotelBooking hotelBooking) {
        this.hotelBooking = hotelBooking;
    }

    public TaxiBooking getTaxiBooking() {
        return taxiBooking;
    }

    public void setTaxiBooking(TaxiBooking taxiBooking) {
        this.taxiBooking = taxiBooking;
    }

    @Override
    public String toString() {
        return "TravelAgent{" +
                "flightBooking=" + flightBooking +
                ", hotelBooking=" + hotelBooking +
                ", taxiBooking=" + taxiBooking +
                '}';
    }
}
