package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity;

import java.util.Date;
import java.util.Objects;

/**
 * @Classname GuestBooking
 * @Description 只用来进行序列化
 * @Date 2022/11/20 21:42
 * @Created by 10835
 */
public class GuestBooking {
    private Customer customer;
    private Date date;
    private Hotel hotel;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuestBooking)) return false;
        GuestBooking that = (GuestBooking) o;
        return Objects.equals(getCustomer(), that.getCustomer()) && Objects.equals(getDate(), that.getDate()) && Objects.equals(getHotel(), that.getHotel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomer(), getDate(), getHotel());
    }

    @Override
    public String toString() {
        return "GuestBooking{" +
                "customer=" + customer +
                ", date=" + date +
                ", hotel=" + hotel +
                '}';
    }
}
