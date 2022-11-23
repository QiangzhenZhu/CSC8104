package uk.ac.newcastle.enterprisemiddleware.coursework.flight;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import java.util.List;

@Path("/api/flightBooking")
@RegisterRestClient(configKey = "flight-booking-api")
public interface FlightBookingService {

    @GET
    List<FlightBooking> getFlightBookings();


    @GET
    @Path("/{id:[0-9]+}")
    FlightBooking getFlightBookingById(@PathParam("id") Long id);

    @GET
    @Path("/customerId/{customerId:[0-9]+}")
    List<FlightBooking> getFlightBookingsByCustomer(@PathParam("customerId") Long id);

    @POST
    @Path("/createFlightBooking")
    FlightBooking createFlightBooking(FlightBooking flightbooking);

    @DELETE
    @Path("/deleteFlightBooking/{id:[0-9]+}")
    FlightBooking deleteFlightBooking(@PathParam("id") Long id);
}