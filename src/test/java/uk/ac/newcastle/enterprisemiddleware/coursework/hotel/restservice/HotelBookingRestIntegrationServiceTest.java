package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.restservice;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Hotel;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.HotelBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.service.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.service.HotelService;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Classname HotelBookingRestIntegrationServiceTest
 * @Description TODO
 * @Date 2022/11/21 14:46
 * @Created by 10835
 */
@QuarkusTest
@TestHTTPEndpoint(HotelBookingRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
class HotelBookingRestIntegrationServiceTest {

    private static HotelBooking hotelBooking;
    private static Hotel hotel;
    private static Customer customer;
    @Inject
    CustomerService customerService;
    @Inject
    HotelService hotelService;

    @BeforeAll
    static void setUp() {
        // get next day in the future
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, 1);
        hotelBooking = new HotelBooking();
        hotelBooking.setBookingDate(c.getTime());
        hotelBooking.setCustomerId(1L);
        hotelBooking.setHotelId(1L);

    }

//    @Test
//    @Order(1)
//    void testCanCreateHotelBooking() throws Exception {
//        hotelService.create(hotel);
//        customerService.create(customer);
//        given().
//                contentType(ContentType.JSON).
//                body(hotelBooking).
//                when()
//                .post().
//                then().
//                statusCode(201);
//    }
//
//
//    @Test
//    @Order(2)
//    public void testCanGetHotelBooking() {
//        Response response = when().
//                get().
//                then().
//                statusCode(200).
//                extract().response();
//
//        HotelBooking[] result = response.body().as(HotelBooking[].class);
//        HotelBooking hotelBookingResult = new HotelBooking();
//        for (HotelBooking item:result) {
//            if (item.equals(hotelBooking)){
//                hotelBookingResult = item;
//            }
//        }
//        System.out.println(hotelBookingResult);
//
//        assertEquals(hotelBooking.getHotelId().longValue(), hotelBookingResult.getHotelId().longValue(), "HotelId not equal");
//        assertTrue(isSameDay(hotelBooking.getBookingDate(), hotelBookingResult.getBookingDate()), "BookingDate not equal");
//        assertEquals(hotelBooking.getCustomerId().longValue(), hotelBookingResult.getCustomerId().longValue(), "CustomerId not equal");
//    }
//
//    @Test
//    @Order(3)
//    public void testDuplicateHotelBookedCausesError() {
//        given().
//                contentType(ContentType.JSON).
//                body(hotelBooking).
//                when().
//                post().
//                then().
//                statusCode(409).
//                body("reasons.hotel", containsString("hotel has been booked"));
//    }

    @Test
    @Order(4)
    public void testCanDeleteHotelBooking() {
        Response response = when().
                get().
                then().
                statusCode(200).
                extract().response();

        HotelBooking[] result = response.body().as(HotelBooking[].class);

        when().
                delete(result[result.length-1].getBookingId().toString()).
                then().
                statusCode(204);
    }

    /**
     * is a same day
     * @param date1
     * @param date2
     * @return
     */
    static boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate localDate2 = date2.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();    return localDate1.isEqual(localDate2);
    }
}