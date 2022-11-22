package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * @Classname HotelBooking
 * @Description TODO
 * @Date 2022/11/20 13:53
 * @Created by 10835
 * ● Customer/Hotel/Booking id: a Long, which should not be settable or changable by an api consumer.
 * ● 客户/酒店/预订的ID：一个长的，不应该被api消费者设置或改变。
 *● Booking hotel & date: A combination of the hotel for which a booking is made and the date for which it is made should be unique *.
 * ● 预订酒店和日期：预订的酒店和预订的日期的组合应该是唯一的。
 *
 */
@Entity
@NamedQueries({
        @NamedQuery(name = HotelBooking.FIND_ALL, query = "SELECT h FROM HotelBooking h ORDER BY h.bookingDate ASC"),
        @NamedQuery(name = HotelBooking.FIND_BY_CUSTOMER_ID, query = "SELECT h FROM HotelBooking h WHERE h.customerId = :customer_id"),
        @NamedQuery(name = HotelBooking.FIND_BY_BOOKING_DATA_AND_HOTEL_ID, query = "SELECT h FROM HotelBooking h WHERE h.bookingDate = :booking_date AND h.hotelId = :hotel_id "),
        @NamedQuery(name = HotelBooking.FIND_BY_CUSTOMER_EMAIL, query = "SELECT h FROM HotelBooking h WHERE h.customerId IN (SELECT c.customerId FROM Customer c WHERE c.email = :email) ")
})
@XmlRootElement
@Table(name = "hotel_booking", uniqueConstraints = @UniqueConstraint(columnNames = {"hotel_id","booking_date"}))
public class HotelBooking implements Serializable {

    private static final long serialVersionUID = 6759320586948001097L;

    public static final String FIND_ALL = "HotelBooking.findAll";
    public static final String FIND_BY_CUSTOMER_ID = "HotelBooking.findByCustomerID";
    public static final String FIND_BY_BOOKING_DATA_AND_HOTEL_ID = "HotelBooking.findByBookingDataAndHotelId";
    public static final String FIND_BY_CUSTOMER_EMAIL = "HotelBooking.findByCustomerEmail";
    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @NotNull
    @Column(name = "customer_id")
    private Long customerId;

    @NotNull
    @Column(name = "hotel_id")
    private Long hotelId;


    @NotNull
    @Future(message = "Booking date can not be in the past. Please choose one in the future")
    @Column(name = "booking_date")
    @Temporal(TemporalType.DATE)
    private Date bookingDate;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }



    public Date getBookingDate() {

        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    @Override
    public String toString() {
        return "HotelBooking{" +
                "bookingId=" + bookingId +
                ", customerId=" + customerId +
                ", hotelId=" + hotelId +
                ", bookingDate=" + bookingDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HotelBooking)) return false;
        HotelBooking that = (HotelBooking) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(hotelId, that.hotelId) && isSameDay(getBookingDate(), that.getBookingDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, hotelId, getBookingDate());
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
