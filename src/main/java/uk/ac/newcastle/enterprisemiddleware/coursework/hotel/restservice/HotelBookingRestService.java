package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.restservice;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.reactive.Cache;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.HotelBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.exception.UniqueHotelBookingDateException;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.service.HotelBookingService;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @Classname HotelBookingRestService
 * @Description TODO
 * @Date 2022/11/20 18:38
 * @Created by 10835
 */
@Path("/hotel_booking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HotelBookingRestService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    HotelBookingService hotelBookingService;

    @GET
    @Operation(summary = "Fetch all booking", description = "Returns a JSON array of all stored HotelBookings objects.")
    public Response retrieveAllHotelBookings() {
        //Create an empty collection to contain the intersection of HotelBookings to be returned
        List<HotelBooking> booking;
        booking = hotelBookingService.findAllOrderedByName();
        

        return Response.ok(booking).build();
    }

    @GET
    @Cache
    @Path("/email/{email:.+[%40|@].+}")
    @Operation(
            summary = "Fetch a Booking by Email",
            description = "Returns a JSON representation of the Booking object with the provided email."
    )
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description ="Booking found"),
            @APIResponse(responseCode = "404", description = "Booking with email not found")
    })
    public Response retrieveHotelBookingsByCustomerEmail(
            @Parameter(description = "email of HotelBooking to be fetched", required = true)
            @PathParam("email")
            String email) {

        List<HotelBooking> hotelBookingList;
        try {
            hotelBookingList = hotelBookingService.findAllByCustomerEmail(email);
        } catch (NoResultException e) {
            // Verify that the contact exists. Return 404, if not present.
            throw new RestServiceException("No HotelBooking with the customer " + email + " was found!", Response.Status.NOT_FOUND);
        }
        return Response.ok(hotelBookingList).build();
    }


    @GET
    @Cache
    @Path("/{id:[0-9]+}")
    @Operation(
            summary = "Fetch a HotelBooking by id",
            description = "Returns a JSON representation of the HotelBooking object with the provided id."
    )
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description ="HotelBooking found"),
            @APIResponse(responseCode = "404", description = "HotelBooking with id not found")
    })
    public Response retrieveHotelById(
            @Parameter(description = "Id of HotelBooking to be fetched")
            @Schema(minimum = "0", required = true)
            @PathParam("id")
            long id) {

        HotelBooking hotel = hotelBookingService.findById(id);
        if (hotel == null) {
            // Verify that the contact exists. Return 404, if not present.
            throw new RestServiceException("No HotelBooking with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        log.info("findById " + id + ": found HotelBooking = " + hotel);

        return Response.ok(hotel).build();
    }


    @SuppressWarnings("unused")
    @POST
    @Operation(description = "Add a new HotelBooking to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "HotelBooking created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid HotelBooking supplied in request body"),
            @APIResponse(responseCode = "409", description = "HotelBooking supplied in request body conflicts with an existing Hotel"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createHotelBooking(
            @Parameter(description = "JSON representation of HotelBooking object to be added to the database", required = true)
            HotelBooking hotelbooking) {

        if (hotelbooking == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {
            // Clear the ID if accidentally set
            hotelbooking.setBookingId(null);

            // Go add the new Hotel.
            hotelBookingService.create(hotelbooking);

            // Create a "Resource Created" 201 Response and pass the contact back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(hotelbooking);


        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        } catch (UniqueHotelBookingDateException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("hotel", "That hotel has been booked in this day");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        }  catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        log.info("createHotelBooking completed. HotelBooking = " + hotelbooking);
        return builder.build();
    }


    @PUT
    @Path("/{id:[0-9]+}")
    @Operation(description = "Update a HotelBooking in the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "HotelBooking updated successfully"),
            @APIResponse(responseCode = "400", description = "Invalid HotelBooking supplied in request body"),
            @APIResponse(responseCode = "404", description = "HotelBooking with id not found"),
            @APIResponse(responseCode = "409", description = "HotelBooking details supplied in request body conflict with another existing Hotel"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response updateHotelBooking(
            @Parameter(description=  "Id of HotelBooking to be updated", required = true)
            @Schema(minimum = "0")
            @PathParam("id")
            long id,
            @Parameter(description = "JSON representation of HotelBooking object to be updated in the database", required = true)
            HotelBooking hotelBooking) {

        if (hotelBooking == null || hotelBooking.getHotelId() == null) {
            throw new RestServiceException("Invalid HotelBooking supplied in request body", Response.Status.BAD_REQUEST);
        }

        if (hotelBooking.getHotelId() != null && hotelBooking.getHotelId() != id) {
            // The client attempted to update the read-only Id. This is not permitted.
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("id", "The HotelBooking ID in the request body must match that of the HotelBooking being updated");
            throw new RestServiceException("HotelBooking details supplied in request body conflict with another Hotel",
                    responseObj, Response.Status.CONFLICT);
        }

        if (hotelBookingService.findById(hotelBooking.getHotelId()) == null) {
            // Verify that the contact exists. Return 404, if not present.
            throw new RestServiceException("No HotelBooking with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        Response.ResponseBuilder builder;

        try {
            // Apply the changes the Hotel.
            hotelBookingService.update(hotelBooking);

            // Create an OK Response and pass the contact back in case it is needed.
            builder = Response.ok(hotelBooking);


        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);
        } catch (UniqueHotelBookingDateException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("hotel", "That hotel has been booked in this day");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        }  catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        log.info("updateHotelBooking completed. HotelBooking = " + hotelBooking);
        return builder.build();
    }


    @DELETE
    @Path("/{id:[0-9]+}")
    @Operation(description = "Delete a HotelBooking from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The hotel has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid HotelBooking id supplied"),
            @APIResponse(responseCode = "404", description = "HotelBooking with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteHotelBooking(
            @Parameter(description = "Id of HotelBooking to be deleted", required = true)
            @Schema(minimum = "0")
            @PathParam("id")
            long id) {

        Response.ResponseBuilder builder;

        HotelBooking hotelBooking = hotelBookingService.findById(id);
        if (hotelBooking == null) {
            // Verify that the contact exists. Return 404, if not present.
            throw new RestServiceException("No HotelBooking with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        try {
            hotelBookingService.delete(hotelBooking);

            builder = Response.noContent();

        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }
        log.info("deleteHotelBooking completed. HotelBooking = " + hotelBooking);
        return builder.build();
    }




}
