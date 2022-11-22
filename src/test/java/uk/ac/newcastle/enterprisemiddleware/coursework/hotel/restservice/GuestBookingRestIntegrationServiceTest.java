package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.restservice;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Hotel;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.GuestBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.HotelBooking;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Classname GuestBookingRestIntegrationServiceTest
 * @Description TODO
 * @Date 2022/11/21 14:46
 * @Created by 10835
 */
@QuarkusTest
@TestHTTPEndpoint(GuestBookingRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
class GuestBookingRestIntegrationServiceTest {

    private static GuestBooking guestBooking;

    @BeforeAll
    static void setUp() {
        // get next day in the future
        guestBooking = new GuestBooking();
        Customer customer = new Customer();
        customer.setFirstName("z");
        customer.setLastName("zq");
        customer.setEmail("1083546145@qq.com");
        customer.setPhoneNumber("00000000010");

        Hotel hotel = new Hotel();
        hotel.setName("alihotel");
        hotel.setPhoneNumber("00000000010");
        hotel.setPostCode("a12345");



        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, 1);
        guestBooking.setCustomer(customer);
        guestBooking.setDate(c.getTime());
        guestBooking.setHotel(hotel);




    }

    @Test
    @Order(1)
    void testCanCreateGuestBooking() {
        given().
                contentType(ContentType.JSON).
                body(guestBooking).
                when()
                .post().
                then().
                statusCode(201);
    }




}