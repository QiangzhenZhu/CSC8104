package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.exception;

import javax.validation.ValidationException;

/**
 * @Classname UniqueHotelPhoneNumberException
 * @Description TODO
 * @Date 2022/11/20 15:53
 * @Created by 10835
 */
public class UniqueHotelPhoneNumberException extends ValidationException {
    public UniqueHotelPhoneNumberException(String message) {
        super(message);
    }

    public UniqueHotelPhoneNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueHotelPhoneNumberException(Throwable cause) {
        super(cause);
    }
}
