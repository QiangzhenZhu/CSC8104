package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Classname Hotel
 * @Description TODO
 * @Date 2022/11/20 13:05
 * @Created by 10835
 * 
 * Hotel name: a non-empty alphabetical string less than 50 characters in length.
 * 酒店名称：长度小于50个字符的非空的字母字符串。
 *
 * Hotel phonenumber: a non-empty string which starts with a 0, contains only digits and is 11 characters in length.
 * 酒店电话号码：一个非空字符串，以0开头，只包含数字，长度为11个字符。
 *
 * Hotel postcode: a non-empty alpha-numerical string which is 6 characters in length.
 * 酒店邮编：一个长度为6个字符的非空的字母数字字符串
 * 
 * 
 * 
 */

@Entity
@NamedQueries({
        @NamedQuery(name = Hotel.FIND_ALL, query = "SELECT c FROM Hotel c ORDER BY c.name ASC"),
        @NamedQuery(name = Hotel.FIND_BY_PHONE_NUMBER, query = "SELECT c FROM Hotel c WHERE c.phoneNumber = :phone_number"),
        @NamedQuery(name = Hotel.FIND_ALL_BY_NAME, query = "SELECT c FROM Hotel c WHERE c.name = :name")
})
@XmlRootElement
@Table(name = "hotel", uniqueConstraints = @UniqueConstraint(columnNames = "phone_number"))
public class Hotel implements Serializable {

    private static final long serialVersionUID = -6609601901970535629L;

    public static final String FIND_ALL = "Hotel.findAll";
    public static final String FIND_BY_PHONE_NUMBER = "Hotel.findByEmail";
    public static final String FIND_ALL_BY_NAME = "Hotel.findAllByName";

    @Id
    @Column(name = "hotel_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId;

    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "[A-Za-z0-9']{6}+", message = "a non-empty alpha-numerical string which is 6 characters in length.")
    @Column(name = "name")
    private String name;
    
    @NotNull
    @Size(min = 11, max = 11)
    @Pattern(regexp = "^0\\d{10}",message = "a non-empty string which starts with a 0, contains only digits and is 11 characters in length")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 6)
    @Pattern(regexp = "[A-Za-z']+", message = "Please use a name without numbers or specials")
    @Column(name = "post_code")
    private String postCode;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hotel)) return false;
        Hotel hotel = (Hotel) o;
        return getName().equals(hotel.getName()) && getPhoneNumber().equals(hotel.getPhoneNumber()) && getPostCode().equals(hotel.getPostCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPhoneNumber(), getPostCode());
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "hotelId=" + hotelId +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", postCode='" + postCode + '\'' +
                '}';
    }
}
