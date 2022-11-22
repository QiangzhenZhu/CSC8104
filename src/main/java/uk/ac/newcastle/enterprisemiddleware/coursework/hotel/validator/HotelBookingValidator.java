package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.validator;

import uk.ac.newcastle.enterprisemiddleware.contact.UniqueEmailException;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.HotelBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.exception.UniqueHotelBookingDateException;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.repository.HotelBookingRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @Classname HotelBookingValidator
 * @Description TODO
 * @Date 2022/11/20 15:54
 * @Created by 10835
 */
@ApplicationScoped
public class HotelBookingValidator extends BaseValidator {
    @Inject
    HotelBookingRepository crud;

    public void validateHotelBooking(HotelBooking hotelBooking) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<HotelBooking>> violations = validator.validate(hotelBooking);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (hotelBookingAlreadyExists(hotelBooking.getBookingDate(), hotelBooking.getHotelId(),hotelBooking.getBookingId())) {
            throw new UniqueHotelBookingDateException("Unique Hotel&Date Violation");
        }
    }

    /**
     * <p>Checks if a contact with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Contact class.</p>
     *
     * <p>Since Update will being using an email that is already in the database we need to make sure that it is the email
     * from the record being updated.</p>
     *
     */
    public boolean hotelBookingAlreadyExists(Date date, Long hotelId,Long hotelBookingId) {
        HotelBooking hotelBooking = null;
        HotelBooking hotelBookingWithID = null;
        try {
            hotelBooking = crud.findByBookingDataAndHotelId(date,hotelId);
        } catch (NoResultException e) {
            // ignore
        }

        if (hotelBooking != null && hotelBookingId != null) {
            try {
                hotelBookingWithID = crud.findById(hotelBookingId);
                if (hotelBookingWithID != null && isSameDay(hotelBookingWithID.getBookingDate(),date) && Objects.equals(hotelBookingWithID.getHotelId(), hotelId)) {
                    hotelBooking = null;
                }
            } catch (NoResultException e) {
                // ignore
            }
        }
        return hotelBooking != null;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate localDate2 = date2.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();    return localDate1.isEqual(localDate2);
    }
}
