package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

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
        @NamedQuery(name = HotelBooking.FIND_BY_CUSTOMER_ID, query = "SELECT h FROM HotelBooking h WHERE h.customer.customerId = :customer_id"),
        @NamedQuery(name = HotelBooking.FIND_BY_BOOKING_DATA_AND_HOTEL_ID, query = "SELECT h FROM HotelBooking h WHERE h.bookingDate = :booking_date AND h.hotel.hotelId = :hotel_id "),
        @NamedQuery(name = HotelBooking.FIND_BY_CUSTOMER_EMAIL, query = "SELECT h FROM HotelBooking h WHERE h.customer.customerId IN (SELECT c.customerId FROM Customer c WHERE c.email = :email) ")
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


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="hotel_id")
    private Hotel hotel = new Hotel();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="customer_id")
    private Customer customer = new Customer();



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

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Long getHotelId(){
        return this.hotel.getHotelId();
    }

    public Long getCustomerId(){
        return this.customer.getCustomerId();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public String toString() {
        return "HotelBooking{" +
                "bookingId=" + bookingId +
                ", hotel=" + hotel +
                ", customer=" + customer +
                ", bookingDate=" + bookingDate +
                '}';
    }
}
