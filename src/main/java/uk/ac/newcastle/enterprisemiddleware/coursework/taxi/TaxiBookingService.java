package uk.ac.newcastle.enterprisemiddleware.coursework.taxi;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import java.util.List;

@Path("/taxiBooking")
@RegisterRestClient(configKey = "taxi-booking-api")
public interface TaxiBookingService {

    @POST
    TaxiBooking createTaxiBooking(TaxiBooking taxiBooking);

    @DELETE
    @Path("/{id:[0-9]+}")
    TaxiBooking deleteTaxiBooking(@PathParam("id") Long id);

    @GET
    List<TaxiBooking> getTaxiBookings();


    @GET
    @Path("/{id:[0-9]+}")
    TaxiBooking getTaxiBookingById(@PathParam("id") Long id);

}
