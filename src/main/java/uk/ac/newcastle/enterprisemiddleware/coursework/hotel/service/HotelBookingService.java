package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.service;


import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.HotelBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.repository.HotelBookingRepository;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.validator.HotelBookingValidator;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

/**
 * @Classname HotelBookingService
 * @Description TODO
 * @Date 2022/11/20 16:31
 * @Created by 10835
 */
@Dependent
public class HotelBookingService extends BaseService {
    @Inject
    HotelBookingValidator validator;

    @Inject
    HotelBookingRepository crud;

    /**
     * <p>Returns a List of all persisted {@link HotelBooking} objects, sorted alphabetically by last name.<p/>
     *
     * @return List of HotelBooking objects
     */
    public List<HotelBooking> findAllOrderedByName() {
        return crud.findAllOrderedByBookingDate();
    }

    public List<HotelBooking> findAllByCustomerEmail(String email) {
        return crud.findByCustomerEmail(email);
    }


    /**
     * <p>Returns a single HotelBooking object, specified by a Long id.<p/>
     *
     * @param id The id field of the HotelBooking to be returned
     * @return The HotelBooking with the specified id
     */
    public HotelBooking findById(Long id) {
        return crud.findById(id);
    }

    /**
     * <p>Returns a single HotelBooking object, specified by a String email.</p>
     *
     * <p>If there is more than one HotelBooking with the specified email, only the first encountered will be returned.<p/>
     *
     * @param customerId The email field of the HotelBooking to be returned
     * @return The first HotelBooking with the specified email
     */
    public HotelBooking findByCustomerId(Long customerId) {
        return crud.findByCustomerId(customerId);
    }

    /**
     * <p>Returns a single HotelBooking object, specified by a String firstName.<p/>
     *
     */
    public HotelBooking findByBookingDataAndHotelId(Date bookingDate, Long hotelId) {
        return crud.findByBookingDataAndHotelId(bookingDate,hotelId);
    }

    /**
     * <p>Writes the provided HotelBooking object to the application database.<p/>
     *
     * <p>Validates the data in the provided HotelBooking object using a {@link HotelBookingValidator} object.<p/>
     *
     * @param hotelBooking The HotelBooking object to be written to the database using a {@link HotelBookingRepository} object
     * @return The HotelBooking object that has been successfully written to the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    public HotelBooking create(HotelBooking hotelBooking) throws Exception {
        log.info("HotelBookingService.create() - Creating " + + hotelBooking.getHotelId() + " " + hotelBooking.getBookingId()+" " +hotelBooking.getBookingDate());

        // Check to make sure the data fits with the parameters in the HotelBooking model and passes validation.
        validator.validateHotelBooking(hotelBooking);

        // Write the hotelbooking to the database.
        return crud.create(hotelBooking);
    }

    /**
     * <p>Updates an existing HotelBooking object in the application database with the provided HotelBooking object.<p/>
     *
     * <p>Validates the data in the provided HotelBooking object using a HotelBookingValidator object.<p/>
     *
     * @param hotelBooking The HotelBooking object to be passed as an update to the application database
     * @return The HotelBooking object that has been successfully updated in the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    public HotelBooking update(HotelBooking hotelBooking) throws Exception {
        log.info("HotelBookingService.update() - Updating " + hotelBooking.getHotelId() + " " + hotelBooking.getCustomerId()+" " +hotelBooking.getBookingDate());

        // Check to make sure the data fits with the parameters in the HotelBooking model and passes validation.
        validator.validateHotelBooking(hotelBooking);

        // Either update the hotelbooking or add it if it can't be found.
        return crud.update(hotelBooking);
    }

    /**
     * <p>Deletes the provided HotelBooking object from the application database if found there.<p/>
     *
     * @param hotelBooking The HotelBooking object to be removed from the application database
     * @return The HotelBooking object that has been successfully removed from the application database; or null
     * @throws Exception
     */
    public HotelBooking delete(HotelBooking hotelBooking) throws Exception {
        log.info("delete() - Deleting " + + hotelBooking.getHotelId() + " " + hotelBooking.getCustomerId()+" "  +hotelBooking.getBookingDate());

        HotelBooking deletedhotelBooking = null;

        if (hotelBooking.getCustomerId() != null) {
            deletedhotelBooking = crud.delete(hotelBooking);
        } else {
            log.info("delete() - No ID was found so can't Delete.");
        }

        return deletedhotelBooking;
    }
}
