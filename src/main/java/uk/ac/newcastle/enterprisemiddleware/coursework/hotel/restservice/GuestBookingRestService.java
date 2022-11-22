package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.restservice;

import io.netty.util.internal.StringUtil;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.wildfly.common.iteration.EnumerationIterator;
import org.yaml.snakeyaml.util.EnumUtils;
import uk.ac.newcastle.enterprisemiddleware.area.InvalidAreaCodeException;
import uk.ac.newcastle.enterprisemiddleware.contact.UniqueEmailException;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.GuestBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Hotel;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.HotelBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.exception.UniqueHotelBookingDateException;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.exception.UniqueHotelPhoneNumberException;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.service.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.service.HotelBookingService;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.service.HotelService;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @Classname GuestBookingRestService
 * @Description TODO
 * @Date 2022/11/21 10:47
 * @Created by 10835
 */

@Path("/booking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GuestBookingRestService {
    @Inject
    @Named("logger")
    Logger log;
    @Inject
    HotelBookingService hotelBookingService;
    @Inject
    CustomerService customerService;
    @Inject
    HotelService hotelService;
    @Inject
    UserTransaction transaction;

    @POST
    @Operation(description = "Create a new booking.if customer not exists,create it")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Booking created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid GuestBooking supplied in request body"),
            @APIResponse(responseCode = "404", description = "hotel with ID  not found"),
            @APIResponse(responseCode = "409", description = "Customer supplied in request body conflicts with an existing Customer"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    //@Transactional
    public Response createGuestBooking(
            @Parameter(description = "JSON representation of GuestBooking object to be added to the database", required = true)
            GuestBooking guestBooking) throws SystemException {
        log.info("GuestBookingRestService -- create " + guestBooking.toString());

        if (guestBooking == null) {
            throw new RestServiceException("Bad Request,GuestBooking is null", Response.Status.BAD_REQUEST);
        }
        if (guestBooking.getCustomer() == null || guestBooking.getHotel() == null || guestBooking.getDate() == null) {
            throw new RestServiceException("Bad Request,Customer or Hotel or Date is null", Response.Status.BAD_REQUEST);
        }
        if (StringUtil.isNullOrEmpty(guestBooking.getCustomer().getEmail()) || StringUtil.isNullOrEmpty(guestBooking.getHotel().getPhoneNumber())) {
            throw new RestServiceException("Bad Request,Customer's email is null or hotel phoneNumber is null", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;
        Long hotelId = null;
        Long customerId = null;
        try {
            transaction.begin();
            Customer customer = guestBooking.getCustomer();
            Customer storeCustomer = null;
            Hotel storedHotel = null;

            try{
                storedHotel = hotelService.findByPhoneNumber(guestBooking.getHotel().getPhoneNumber());

            }catch (Exception e){

            }
            if (storedHotel == null) {
                throw new RestServiceException("Bad Request,can not find the hotel by the phone number", Response.Status.BAD_REQUEST);
            }
            try{
                storeCustomer = customerService.findByEmail(guestBooking.getCustomer().getEmail());
            }catch(Exception e){

            }


            if(storeCustomer == null){
                //no customer find with the email ,so create a new customer
                customer.setCustomerId(null);
                storeCustomer = customerService.create(customer);
            }

            customerId = storeCustomer.getCustomerId();
            hotelId = storedHotel.getHotelId();
            HotelBooking hotelBooking = new HotelBooking();
            hotelBooking.setCustomerId(customerId);
            hotelBooking.setHotelId(hotelId);
            hotelBooking.setBookingId(null);
            hotelBooking.setBookingDate(guestBooking.getDate());
            hotelBookingService.create(hotelBooking);
            builder = Response.status(Response.Status.CREATED).entity(guestBooking);

            transaction.commit();

        } catch (ConstraintViolationException ce) {
            transaction.rollback();
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        } catch (UniqueEmailException e) {
            transaction.rollback();
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "That email is already used, please use a unique email");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        } catch (UniqueHotelBookingDateException e) {
            transaction.rollback();
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("hotel", "The hotel has been booked.");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, e);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            throw new RestServiceException(e);
        }
        log.info("GuestBookingRestService -- createGuestBooking successfully");
        return builder.build();
    }



}
