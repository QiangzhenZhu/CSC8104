package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.restservice;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.reactive.Cache;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Hotel;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.exception.UniqueHotelPhoneNumberException;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.service.HotelService;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @Classname HotelRestService
 * @Description TODO
 * @Date 2022/11/20 18:37
 * @Created by 10835
 */
//@Path("/hotels")
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
public class HotelRestService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    HotelService hotelService;

    @GET
    @Operation(summary = "Fetch all hotels", description = "Returns a JSON array of all stored Hotels objects.")
    public Response retrieveAllHotels(@QueryParam("name") String name) {
        //Create an empty collection to contain the intersection of Hotels to be returned
        List<Hotel> hotels;

        if(name == null ) {
            hotels = hotelService.findAllOrderedByName();
        } else {
            hotels = hotelService.findAllByName(name);
        }

        return Response.ok(hotels).build();
    }

    /**
     * <p>Search for and return a Hotel identified by phoneNumber address.<p/>
     *
     * <p>Path annotation includes very simple regex to differentiate between phoneNumber addresses and Ids.
     * <strong>DO NOT</strong> attempt to use this regex to validate phoneNumber addresses.</p>
     *
     *
     * @param phoneNumber The string parameter value provided as a Hotel's phoneNumber
     * @return A Response containing a single Hotel
     */
    @GET
    @Cache
    @Path("/phoneNumber/{phoneNumber:^0\\d{10}}")
    @Operation(
            summary = "Fetch a Hotel by phone",
            description = "Returns a JSON representation of the Hotel object with the provided phoneNumber."
    )
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description ="Hotel found"),
            @APIResponse(responseCode = "404", description = "Hotel with phoneNumber not found")
    })
    public Response retrieveHotelsByPhoneNumber(
            @Parameter(description = "PhoneNumber of Hotel to be fetched", required = true)
            @PathParam("phoneNumber")
            String phoneNumber) {

        Hotel hotel;
        try {
            hotel = hotelService.findByPhoneNumber(phoneNumber);
        } catch (NoResultException e) {
            // Verify that the contact exists. Return 404, if not present.
            throw new RestServiceException("No Hotel with the phone " + phoneNumber + " was found!", Response.Status.NOT_FOUND);
        }
        return Response.ok(hotel).build();
    }

    /**
     * <p>Search for and return a Hotel identified by id.</p>
     *
     * @param id The long parameter value provided as a Hotel's id
     * @return A Response containing a single Hotel
     */
    @GET
    @Cache
    @Path("/{id:[0-9]+}")
    @Operation(
            summary = "Fetch a Hotel by id",
            description = "Returns a JSON representation of the Hotel object with the provided id."
    )
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description ="Hotel found"),
            @APIResponse(responseCode = "404", description = "Hotel with id not found")
    })
    public Response retrieveHotelById(
            @Parameter(description = "Id of Hotel to be fetched")
            @Schema(minimum = "0", required = true)
            @PathParam("id")
            long id) {

        Hotel hotel = hotelService.findById(id);
        if (hotel == null) {
            // Verify that the contact exists. Return 404, if not present.
            throw new RestServiceException("No Hotel with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        log.info("findById " + id + ": found Hotel = " + hotel);

        return Response.ok(hotel).build();
    }

    /**
     * <p>Creates a new contact from the values provided. Performs validation and will return a JAX-RS response with
     * either 201 (Resource created) or with a map of fields, and related errors.</p>
     *
     * @param hotel The Hotel object, constructed automatically from JSON input, to be <i>created</i> via
     * {@link HotelService#create(Hotel)}
     * @return A Response indicating the outcome of the create operation
     */
    @SuppressWarnings("unused")
    @POST
    @Operation(description = "Add a new Hotel to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Hotel created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid Hotel supplied in request body"),
            @APIResponse(responseCode = "409", description = "Hotel supplied in request body conflicts with an existing Hotel"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createHotel(
            @Parameter(description = "JSON representation of Hotel object to be added to the database", required = true)
            Hotel hotel) {

        if (hotel == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {
            // Clear the ID if accidentally set
            hotel.setHotelId(null);

            // Go add the new Hotel.
            hotelService.create(hotel);

            // Create a "Resource Created" 201 Response and pass the contact back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(hotel);


        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        } catch (UniqueHotelPhoneNumberException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("phoneNumber", "That phoneNumber is already used, please use a unique phoneNumber");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        }  catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        log.info("createHotel completed. Hotel = " + hotel);
        return builder.build();
    }

    /**
     * <p>Updates the contact with the ID provided in the database. Performs validation, and will return a JAX-RS response
     * with either 200 (ok), or with a map of fields, and related errors.</p>
     *
     * @param hotel The Hotel object, constructed automatically from JSON input, to be <i>updated</i> via
     * {@link HotelService#update(Hotel)}
     * @param id The long parameter value provided as the id of the Hotel to be updated
     * @return A Response indicating the outcome of the create operation
     */
    @PUT
    @Path("/{id:[0-9]+}")
    @Operation(description = "Update a Hotel in the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Hotel updated successfully"),
            @APIResponse(responseCode = "400", description = "Invalid Hotel supplied in request body"),
            @APIResponse(responseCode = "404", description = "Hotel with id not found"),
            @APIResponse(responseCode = "409", description = "Hotel details supplied in request body conflict with another existing Hotel"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response updateHotel(
            @Parameter(description=  "Id of Hotel to be updated", required = true)
            @Schema(minimum = "0")
            @PathParam("id")
            long id,
            @Parameter(description = "JSON representation of Hotel object to be updated in the database", required = true)
            Hotel hotel) {

        if (hotel == null || hotel.getHotelId() == null) {
            throw new RestServiceException("Invalid Hotel supplied in request body", Response.Status.BAD_REQUEST);
        }

        if (hotel.getHotelId() != null && hotel.getHotelId() != id) {
            // The client attempted to update the read-only Id. This is not permitted.
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("id", "The Hotel ID in the request body must match that of the Hotel being updated");
            throw new RestServiceException("Hotel details supplied in request body conflict with another Hotel",
                    responseObj, Response.Status.CONFLICT);
        }

        if (hotelService.findById(hotel.getHotelId()) == null) {
            // Verify that the contact exists. Return 404, if not present.
            throw new RestServiceException("No Hotel with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        Response.ResponseBuilder builder;

        try {
            // Apply the changes the Hotel.
            hotelService.update(hotel);

            // Create an OK Response and pass the contact back in case it is needed.
            builder = Response.ok(hotel);


        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);
        } catch (UniqueHotelPhoneNumberException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("phoneNumber", "That phoneNumber is already used, please use a unique phoneNumber");
            throw new RestServiceException("C details supplied in request body conflict with another Hotel",
                    responseObj, Response.Status.CONFLICT, e);
        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        log.info("updateHotel completed. Hotel = " + hotel);
        return builder.build();
    }

    /**
     * <p>Deletes a hotel using the ID provided. If the ID is not present then nothing can be deleted.</p>
     *
     * <p>Will return a JAX-RS response with either 204 NO CONTENT or with a map of fields, and related errors.</p>
     *
     * @param id The Long parameter value provided as the id of the Hotel to be deleted
     * @return A Response indicating the outcome of the delete operation
     */
    @DELETE
    @Path("/{id:[0-9]+}")
    @Operation(description = "Delete a Hotel from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The hotel has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid Hotel id supplied"),
            @APIResponse(responseCode = "404", description = "Hotel with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteHotel(
            @Parameter(description = "Id of Hotel to be deleted", required = true)
            @Schema(minimum = "0")
            @PathParam("id")
            long id) {

        Response.ResponseBuilder builder;

        Hotel hotel = hotelService.findById(id);
        if (hotel == null) {
            // Verify that the contact exists. Return 404, if not present.
            throw new RestServiceException("No Hotel with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        try {
            hotelService.delete(hotel);

            builder = Response.noContent();

        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }
        log.info("deleteHotel completed. Hotel = " + hotel);
        return builder.build();
    }
}
