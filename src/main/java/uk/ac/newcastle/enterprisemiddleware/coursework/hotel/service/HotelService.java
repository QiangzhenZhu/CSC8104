package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.service;

import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Hotel;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.repository.HotelRepository;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.validator.HotelValidator;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @Classname HotelService
 * @Description TODO
 * @Date 2022/11/20 16:30
 * @Created by 10835
 */
@Dependent
public class HotelService extends BaseService{
    @Inject
    HotelValidator validator;

    @Inject
    HotelRepository crud;

    /**
     * <p>Returns a List of all persisted {@link Hotel} objects, sorted alphabetically by last name.<p/>
     *
     * @return List of Hotel objects
     */
    public List<Hotel> findAllOrderedByName() {
        return crud.findAllOrderedByName();
    }

    /**
     * <p>Returns a single Hotel object, specified by a Long id.<p/>
     *
     * @param id The id field of the Hotel to be returned
     * @return The Hotel with the specified id
     */
    public Hotel findById(Long id) {
        return crud.findById(id);
    }

    /**
     * <p>Returns a single Hotel object, specified by a String email.</p>
     *
     * <p>If there is more than one Hotel with the specified email, only the first encountered will be returned.<p/>
     *
     * @param phoneNumber The email field of the Hotel to be returned
     * @return The first Hotel with the specified email
     */
    public Hotel findByPhoneNumber(String phoneNumber) {
        return crud.findByPhoneNumber(phoneNumber);
    }

    /**
     * <p>Returns a single Hotel object, specified by a String firstName.<p/>
     *
     * @param name The firstName field of the Hotel to be returned
     * @return The first Hotel with the specified firstName
     */
    public List<Hotel> findAllByName(String name) {
        return crud.findAllByName(name);
    }
    

    /**
     * <p>Writes the provided Hotel object to the application database.<p/>
     *
     * <p>Validates the data in the provided Hotel object using a {@link HotelValidator} object.<p/>
     *
     * @param hotel The Hotel object to be written to the database using a {@link HotelRepository} object
     * @return The Hotel object that has been successfully written to the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    public Hotel create(Hotel hotel) throws Exception {
        log.info("HotelService.create() - Creating " + hotel.toString());

        // Check to make sure the data fits with the parameters in the Hotel model and passes validation.
        validator.validateHotel(hotel);

        // Write the contact to the database.
        return crud.create(hotel);
    }

    /**
     * <p>Updates an existing Hotel object in the application database with the provided Hotel object.<p/>
     *
     * <p>Validates the data in the provided Hotel object using a HotelValidator object.<p/>
     *
     * @param hotel The Hotel object to be passed as an update to the application database
     * @return The Hotel object that has been successfully updated in the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    public Hotel update(Hotel hotel) throws Exception {
        log.info("HotelService.update() - Updating " + hotel.toString());

        // Check to make sure the data fits with the parameters in the Hotel model and passes validation.
        validator.validateHotel(hotel);

        // Either update the contact or add it if it can't be found.
        return crud.update(hotel);
    }

    /**
     * <p>Deletes the provided Hotel object from the application database if found there.<p/>
     *
     * @param hotel The Hotel object to be removed from the application database
     * @return The Hotel object that has been successfully removed from the application database; or null
     * @throws Exception
     */
    public Hotel delete(Hotel hotel) throws Exception {
        log.info("delete() - Deleting " + hotel.toString());

        Hotel deletedHotel = null;

        if (hotel.getHotelId() != null) {
            deletedHotel = crud.delete(hotel);
        } else {
            log.info("delete() - No ID was found so can't Delete.");
        }

        return deletedHotel;
    }
}
