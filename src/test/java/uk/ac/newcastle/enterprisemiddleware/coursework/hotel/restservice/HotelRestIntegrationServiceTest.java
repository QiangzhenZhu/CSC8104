package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.restservice;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Hotel;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @Classname HotelRestIntegrationServiceTest
 * @Description TODO
 * @Date 2022/11/21 14:47
 * @Created by 10835
 */
@QuarkusTest
@TestHTTPEndpoint(HotelRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
class HotelRestIntegrationServiceTest {

    private static Hotel hotel;
    @BeforeAll
    static void setUp() {
        hotel = new Hotel();
        hotel.setName("yijia");
        hotel.setPhoneNumber("01121212341");
        hotel.setPostCode("iuytyy");
        
    }

    @Test
    @Order(1)
    void testCanCreateHotel() {
        given().
                contentType(ContentType.JSON).
                body(hotel).
                when()
                .post().
                then().
                statusCode(201);
    }


    @Test
    @Order(2)
    public void testCanGetHotels() {
        Response response = when().
                get().
                then().
                statusCode(200).
                extract().response();

        Hotel[] result = response.body().as(Hotel[].class);


        Hotel hotelResult = new Hotel();
        for (Hotel item:result) {
            if (item.equals(hotel)){
                hotelResult = item;
            }
        }
        System.out.println(hotelResult);


        assertTrue(hotel.getName().equals(hotelResult.getName()), "name not equal");
        assertTrue(hotel.getPhoneNumber().equals(hotelResult.getPhoneNumber()), "PhoneNumber not equal");
        assertTrue(hotel.getPostCode().equals(hotelResult.getPostCode()), "PostCode not equal");
    }


    @Test
    @Order(3)
    public void testCanGetHotelByPhoneNumber() {
        Response response = when().
                get("/phoneNumber/"+hotel.getPhoneNumber()).
                then().
                statusCode(200).
                extract().response();

        Hotel hotelResult = response.body().as(Hotel.class);


        assertTrue(hotel.getName().equals(hotelResult.getName()), "name not equal");
        assertTrue(hotel.getPhoneNumber().equals(hotelResult.getPhoneNumber()), "PhoneNumber not equal");
        assertTrue(hotel.getPostCode().equals(hotelResult.getPostCode()), "PostCode not equal");
    }

    @Test
    @Order(4)
    public void testDuplicatePhoneNumberCausesError() {
        given().
                contentType(ContentType.JSON).
                body(hotel).
                when().
                post().
                then().
                statusCode(409).
                body("reasons.phoneNumber", containsString("phoneNumber is already used"));
    }

    @Test
    @Order(5)
    public void testCanDeleteHotel() {
        Response response = when().
                get().
                then().
                statusCode(200).
                extract().response();

        Hotel[] result = response.body().as(Hotel[].class);

        when().
                delete(result[result.length-1].getHotelId().toString()).
                then().
                statusCode(204);
    }
}