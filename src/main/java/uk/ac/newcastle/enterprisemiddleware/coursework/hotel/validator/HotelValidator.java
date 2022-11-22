package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.validator;

import uk.ac.newcastle.enterprisemiddleware.contact.UniqueEmailException;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Hotel;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.exception.UniqueHotelPhoneNumberException;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.repository.HotelRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Classname HotelValidator
 * @Description TODO
 * @Date 2022/11/20 15:54
 * @Created by 10835
 */
@ApplicationScoped
public class HotelValidator extends BaseValidator {
    @Inject
    HotelRepository crud;

    public void validateHotel(Hotel hotel) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Hotel>> violations = validator.validate(hotel);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (phoneNumberAlreadyExists(hotel.getPhoneNumber(), hotel.getHotelId())) {
            throw new UniqueHotelPhoneNumberException("Unique PhoneNumber Violation");
        }
    }

    /**
     * <p>Checks if a contact with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Contact class.</p>
     *
     * <p>Since Update will being using an email that is already in the database we need to make sure that it is the email
     * from the record being updated.</p>
     *
     * @param phoneNumber The email to check is unique
     * @param id The user id to check the email against if it was found
     * @return boolean which represents whether the email was found, and if so if it belongs to the user with id
     */
    public boolean phoneNumberAlreadyExists(String phoneNumber, Long id) {
        Hotel hotel = null;
        Hotel hotelWithID = null;
        try {
            hotel = crud.findByPhoneNumber(phoneNumber);
        } catch (NoResultException e) {
            // ignore
        }

        if (phoneNumber != null && id != null) {
            try {
                hotelWithID = crud.findById(id);
                if (hotelWithID != null && hotelWithID.getPhoneNumber().equals(phoneNumber)) {
                    hotel = null;
                }
            } catch (NoResultException e) {
                // ignore
            }
        }
        return hotel != null;
    }
}
