package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.restservice;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Customer;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Classname CustomerRestIntegrationServiceTest
 * @Description TODO
 * @Date 2022/11/21 14:38
 * @Created by 10835
 */
@QuarkusTest
@TestHTTPEndpoint(CustomerRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
class CustomerRestIntegrationServiceTest {

    private static Customer customer;
    @BeforeAll
    static void setUp() {
        customer = new Customer();
        customer.setFirstName("z");
        customer.setLastName("zq");
        customer.setEmail("1083546145@qq.com");
        customer.setPhoneNumber("01111111111");
    }

    @Test
    @Order(1)
    void testCanCreateCustomer() {
        given().
                contentType(ContentType.JSON).
                body(customer).
                when()
                .post().
                then().
                statusCode(201);
    }


    @Test
    @Order(2)
    public void testCanGetCustomers() {
        Response response = when().
                get().
                then().
                statusCode(200).
                extract().response();

        Customer[] result = response.body().as(Customer[].class);
        Customer customerResult = new Customer();
        for (Customer item:result) {
            if (item.equals(customer)){
                customerResult = item;
            }
        }

        System.out.println(result[result.length-1]);

        assertTrue(customer.getFirstName().equals(customerResult.getFirstName()), "First name not equal");
        assertTrue(customer.getLastName().equals(customerResult.getLastName()), "Last name not equal");
        assertTrue(customer.getEmail().equals(customerResult.getEmail()), "Email not equal");
        assertTrue(customer.getPhoneNumber().equals(customerResult.getPhoneNumber()), "Phone number not equal");
    }

    @Test
    @Order(3)
    public void testDuplicateEmailCausesError() {
        given().
                contentType(ContentType.JSON).
                body(customer).
                when().
                post().
                then().
                statusCode(409).
                body("reasons.email", containsString("email is already used"));
    }

    @Test
    @Order(4)
    public void testCanDeleteCustomer() {
        Response response = when().
                get().
                then().
                statusCode(200).
                extract().response();

        Customer[] result = response.body().as(Customer[].class);

        when().
                delete(result[result.length-1].getCustomerId().toString()).
                then().
                statusCode(204);
    }
}