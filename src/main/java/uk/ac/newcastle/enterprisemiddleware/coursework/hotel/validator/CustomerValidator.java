package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.validator;

import uk.ac.newcastle.enterprisemiddleware.contact.UniqueEmailException;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.repository.CustomerRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Classname CustomerValidator
 * @Description TODO
 * @Date 2022/11/20 15:54
 * @Created by 10835
 */
@ApplicationScoped
public class CustomerValidator extends BaseValidator{
    @Inject
    CustomerRepository crud;

    public void validateCustomer(Customer customer) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(customer.getEmail(), customer.getCustomerId())) {
            throw new UniqueEmailException("Unique Email Violation");
        }
    }

    /**
     * <p>Checks if a contact with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Contact class.</p>
     *
     * <p>Since Update will being using an email that is already in the database we need to make sure that it is the email
     * from the record being updated.</p>
     *
     * @param email The email to check is unique
     * @param id The user id to check the email against if it was found
     * @return boolean which represents whether the email was found, and if so if it belongs to the user with id
     */
    public boolean emailAlreadyExists(String email, Long id) {
        Customer customer = null;
        Customer customerWithID = null;
        try {
            customer = crud.findByEmail(email);
        } catch (NoResultException e) {
            // ignore
        }

        if (customer != null && id != null) {
            try {
                customerWithID = crud.findById(id);
                if (customerWithID != null && customerWithID.getEmail().equals(email)) {
                    customer = null;
                }
            } catch (NoResultException e) {
                // ignore
            }
        }
        return customer != null;
    }
}
