package uk.ac.newcastle.enterprisemiddleware.coursework.agent;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import uk.ac.newcastle.enterprisemiddleware.coursework.flight.FlightBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.flight.FlightBookingService;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.HotelBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.service.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.service.HotelBookingService;
import uk.ac.newcastle.enterprisemiddleware.coursework.taxi.TaxiBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.taxi.TaxiBookingService;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/**
 * @description 旅行社接口服务类
 */

@Path("travelAgentBooking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TravelAgentBookingRestService {

    @Inject
    Logger log;

    @Inject
    HotelBookingService hotelBookingService;

    @Inject
    CustomerService customerservice;

    @RestClient
    TaxiBookingService taxiBookingService;

    @RestClient
    FlightBookingService flightBookingService;

    @Inject
    TravelAgentBookingRepository travelAgentBookingRepository;

    @GET
    @Path("/findAllTravelAgentBookings")
    @Operation(summary = "Fetch all TravelAgentBooking", description = "Returns a JSON array of all stored TravelAgentBooking objects.")
    public Response findAllBookings() {
        List<TravelAgentBooking> travelAgentBookings = travelAgentBookingRepository.findAllTravelAgentBook();
        return Response.ok(travelAgentBookings).build();
    }

    @GET
    @Path("/findTravelAgentByCustomerId/{id:[0-9]+}")
    @Operation(summary = "Fetch all TravelAgent", description = "Returns a JSON array of all stored TravelAgent objects.")
    public Response findBookingsByCustomerId(@PathParam("id") Long customerId) {
        if (customerId == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        Customer customer = customerservice.findById(customerId);
        if (customer == null) {
            throw new RestServiceException("No Customer with the customerId " + customerId + " was found!", Response.Status.NOT_FOUND);
        }
        List<TravelAgentBooking> travelAgentBookings = travelAgentBookingRepository.findAllByCustomer(customerId);
        return Response.ok(travelAgentBookings).build();
    }

    @DELETE
    @Path("/deleteTravelAgentBookingById/{id:[0-9]+}")
    @Operation(description = "Delete TravelAgentBooking")
    @APIResponses(value = {@APIResponse(responseCode = "200", description = "TravelAgent delete successfully.")})
    @Transactional
    public Response deleteTravelAgentBookingById(@PathParam("id") Long id) throws SystemException {
        TravelAgentBooking travelAgentBooking = travelAgentBookingRepository.findTravelAgentBookById(id);
        if (travelAgentBooking == null) {
            throw new RestServiceException("TravelAgentBooking not exist", Response.Status.BAD_REQUEST);
        }
        Customer customer = customerservice.findById(travelAgentBooking.getCustomerId());
        if (customer == null) {
            throw new RestServiceException("Customer not exist", Response.Status.BAD_REQUEST);
        }
        Response response;
        try {
            flightBookingService.deleteFlightBooking(travelAgentBooking.getHotelBookingId());
            taxiBookingService.deleteTaxiBooking(travelAgentBooking.getTaxiBookingId());
            HotelBooking hotelBooking = hotelBookingService.findById(travelAgentBooking.getHotelBookingId());
            hotelBookingService.delete(hotelBooking);
            travelAgentBookingRepository.delete(travelAgentBooking);
            response = Response.ok(travelAgentBooking).build();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RestServiceException(e);
        }
        return response;
    }

    @POST
    @Path("/createTravelAgentBooking")
    @Operation(description = "Create a new TravelAgentBooking")
    @Transactional
    public Response createTravelAgentBooking(@Valid TravelAgent travelAgent) throws Exception {
        TaxiBooking taxiBooking = null;
        FlightBooking flightBooking = null;
        HotelBooking hotelBooking = null;

        if (travelAgent == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        log.info(travelAgent.toString());
        Response res = null;
        try {
            hotelBooking = travelAgent.getHotelBooking();
            hotelBookingService.create(hotelBooking);


            taxiBooking = taxiBookingService.createTaxiBooking(travelAgent.getTaxiBooking());
            if (taxiBooking == null) {
                throw new RestServiceException("Sorry, Taxi booking error");
            }

            flightBooking = flightBookingService.createFlightBooking(travelAgent.getFlightBooking());
            if (flightBooking == null) {
                throw new RestServiceException("Sorry,flight booking error");
            }


            TravelAgentBooking travelAgentBooking = new TravelAgentBooking();
            travelAgentBooking.setCustomerId(travelAgent.getCustomer().getCustomerId());
            travelAgentBooking.setTaxiBookingId(taxiBooking.getId());
            travelAgentBooking.setFlightBookingId(flightBooking.getId());
            travelAgentBooking.setHotelBookingId(hotelBooking.getBookingId());
            travelAgentBookingRepository.create(travelAgentBooking);
            // all successful , commit transaction
            res = Response.status(Response.Status.CREATED).entity(travelAgentBooking).build();

        } catch (Exception e) {
            e.printStackTrace();
            log.severe(e.getMessage());
            log.severe("database rollback");
            //roll back ,飞机预定失败或出租车预定失败
            if (hotelBooking!=null &&(flightBooking == null || taxiBooking == null)) {
                hotelBookingService.delete(hotelBooking);
                if (flightBooking != null) {
                    flightBookingService.deleteFlightBooking(flightBooking.getId());
                }
                if (taxiBooking != null) {
                    taxiBookingService.deleteTaxiBooking(taxiBooking.getId());
                }
            }
            throw new RestServiceException(e.getMessage());
        }
        return res;
    }
}
