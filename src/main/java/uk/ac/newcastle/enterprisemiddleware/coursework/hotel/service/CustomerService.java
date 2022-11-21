package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.service;

import uk.ac.newcastle.enterprisemiddleware.contact.Contact;
import uk.ac.newcastle.enterprisemiddleware.contact.ContactRepository;
import uk.ac.newcastle.enterprisemiddleware.contact.ContactValidator;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.repository.CustomerRepository;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.validator.CustomerValidator;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @Classname CustomerService
 * @Description TODO
 * @Date 2022/11/20 16:30
 * @Created by 10835
 */
@Dependent
public class CustomerService extends BaseService{
    @Inject
    CustomerValidator validator;

    @Inject
    CustomerRepository crud;

    /**
     * <p>Returns a List of all persisted {@link Contact} objects, sorted alphabetically by last name.<p/>
     *
     * @return List of Contact objects
     */
    public List<Customer> findAllOrderedByName() {
        return crud.findAllOrderedByName();
    }

    /**
     * <p>Returns a single Contact object, specified by a Long id.<p/>
     *
     * @param id The id field of the Contact to be returned
     * @return The Contact with the specified id
     */
    public Customer findById(Long id) {
        return crud.findById(id);
    }

    /**
     * <p>Returns a single Contact object, specified by a String email.</p>
     *
     * <p>If there is more than one Contact with the specified email, only the first encountered will be returned.<p/>
     *
     * @param email The email field of the Contact to be returned
     * @return The first Contact with the specified email
     */
    public Customer findByEmail(String email) {
        return crud.findByEmail(email);
    }

    /**
     * <p>Returns a single Contact object, specified by a String firstName.<p/>
     *
     * @param firstName The firstName field of the Contact to be returned
     * @return The first Contact with the specified firstName
     */
    public List<Customer> findAllByFirstName(String firstName) {
        return crud.findAllByFirstName(firstName);
    }

    /**
     * <p>Returns a single Contact object, specified by a String lastName.<p/>
     *
     * @param lastName The lastName field of the Contacts to be returned
     * @return The Contacts with the specified lastName
     */
    public List<Customer> findAllByLastName(String lastName) {
        return crud.findAllByLastName(lastName);
    }

    /**
     * <p>Writes the provided Contact object to the application database.<p/>
     *
     * <p>Validates the data in the provided Contact object using a {@link ContactValidator} object.<p/>
     *
     * @param customer The Contact object to be written to the database using a {@link ContactRepository} object
     * @return The Contact object that has been successfully written to the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    public Customer create(Customer customer) throws Exception {
        log.info("ContactService.create() - Creating " + customer.getFirstName() + " " + customer.getLastName());

        // Check to make sure the data fits with the parameters in the Contact model and passes validation.
        validator.validateCustomer(customer);

        // Write the customer to the database.
        return crud.create(customer);
    }

    /**
     * <p>Updates an existing Contact object in the application database with the provided Contact object.<p/>
     *
     * <p>Validates the data in the provided Contact object using a ContactValidator object.<p/>
     *
     * @param customer The Contact object to be passed as an update to the application database
     * @return The Contact object that has been successfully updated in the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    public Customer update(Customer customer) throws Exception {
        log.info("ContactService.update() - Updating " + customer.getFirstName() + " " + customer.getLastName());

        // Check to make sure the data fits with the parameters in the Contact model and passes validation.
        validator.validateCustomer(customer);

        // Either update the customer or add it if it can't be found.
        return crud.update(customer);
    }

    /**
     * <p>Deletes the provided Contact object from the application database if found there.<p/>
     *
     * @param customer The Contact object to be removed from the application database
     * @return The Contact object that has been successfully removed from the application database; or null
     * @throws Exception
     */
    public Customer delete(Customer customer) throws Exception {
        log.info("delete() - Deleting " + customer.toString());

        Customer deletedCustomer = null;

        if (customer.getCustomerId() != null) {
            deletedCustomer = crud.delete(customer);
        } else {
            log.info("delete() - No ID was found so can't Delete.");
        }

        return deletedCustomer;
    }
}
