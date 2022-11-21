package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Classname Customer
 * @Description TODO
 * @Date 2022/11/20 13:02
 * @Created by 10835
 * ● Customer name: a non-empty alphabetical string less than 50 characters in length.
 * ● 客户名称：一个长度小于50个字符的非空的字母串。
 *
 * ● Customer email: a non-empty string which is a valid email address.
 * ● 客户电子邮件：一个非空的字符串，是一个有效的电子邮件地址。
 *
 * ● Customer phonenumber : a non-empty string which starts with a 0, contains only digits and is 11 characters in length.
 * ● 客户电话号码：一个非空字符串，以0开头，只包含数字，长度为11个字符。
 *
 *
 *
 *
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Customer.FIND_ALL, query = "SELECT c FROM Customer c ORDER BY c.lastName ASC, c.firstName ASC"),
        @NamedQuery(name = Customer.FIND_BY_EMAIL, query = "SELECT c FROM Customer c WHERE c.email = :email")
})
@XmlRootElement
@Table(name = "customer", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Customer implements Serializable {
    private static final long serialVersionUID = 6755681865638683046L;

    public static final String FIND_ALL = "Customer.findAll";
    public static final String FIND_BY_EMAIL = "Customer.findByEmail";

    @Id
    @Column(name="customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z']+", message = "Please use a name without numbers or specials")
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z']+", message = "Please use a name without numbers or specials")
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @NotEmpty
    @Email(message = "The email address must be in the format of name@domain.com")
    @Column(name = "email")
    private String email;

    @NotNull
    @Size(min = 11, max = 11)
    @Pattern(regexp = "^0\\d{10}",message = "a non-empty string which starts with a 0, contains only digits and is 11 characters in length")
    @Column(name = "phone_number")
    private String phoneNumber;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return getFirstName().equals(customer.getFirstName()) && getLastName().equals(customer.getLastName()) && getEmail().equals(customer.getEmail()) && getPhoneNumber().equals(customer.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getEmail(), getPhoneNumber());
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
