package uk.ac.newcastle.enterprisemiddleware.coursework.taxi;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 出租车预定类
 */

public class TaxiBooking implements Serializable {

    private Long id;

    private Long customerId;

    private Date bookingDate;

    private Long taxiId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Long getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(Long taxiId) {
        this.taxiId = taxiId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TaxiBooking{");
        sb.append("id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", bookingDate=").append(bookingDate);
        sb.append(", taxiId=").append(taxiId);
        sb.append('}');
        return sb.toString();
    }
}
