package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.exception;

import javax.validation.ValidationException;

/**
 * @Classname UniqueHotelBookingDateException
 * @Description TODO
 * @Date 2022/11/20 15:17
 * @Created by 10835
 */
public class UniqueHotelBookingDateException extends ValidationException {
    public UniqueHotelBookingDateException(String message) {
        super(message);
    }

    public UniqueHotelBookingDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueHotelBookingDateException(Throwable cause) {
        super(cause);
    }
}
