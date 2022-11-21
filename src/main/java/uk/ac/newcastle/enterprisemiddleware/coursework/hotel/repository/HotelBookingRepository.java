package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.repository;

import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.HotelBooking;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;

import java.util.Date;
import java.util.List;

/**
 * @Classname HotelBookingRepository
 * @Description TODO
 * @Date 2022/11/20 15:55
 * @Created by 10835
 */
@RequestScoped
public class HotelBookingRepository extends BaseRepository{
    /**
     * <p>Returns a List of all persisted {@link HotelBooking} objects, sorted alphabetically by last name.</p>
     *
     * @return List of HotelBooking objects
     */
    public List<HotelBooking> findAllOrderedByBookingDate() {
        TypedQuery<HotelBooking> query = em.createNamedQuery(HotelBooking.FIND_ALL, HotelBooking.class);
        return query.getResultList();
    }

    /**
     * <p>Returns a single HotelBooking object, specified by a Long id.<p/>
     *
     * @param id The id field of the HotelBooking to be returned
     * @return The HotelBooking with the specified id
     */
    public HotelBooking findById(Long id) {
        return em.find(HotelBooking.class, id);
    }

    /**
     * <p>Returns a single HotelBooking object, specified by a String email.</p>
     *
     * <p>If there is more than one HotelBooking with the specified email, only the first encountered will be returned.<p/>
     *
     * @param customerId The email field of the HotelBooking to be returned
     * @return The first HotelBooking with the specified email
     */
    public HotelBooking findByCustomerId(Long customerId) {
        TypedQuery<HotelBooking> query = em.createNamedQuery(HotelBooking.FIND_BY_CUSTOMER_ID, HotelBooking.class).setParameter("customer_id", customerId);
        return query.getSingleResult();
    }


    public HotelBooking findByBookingDataAndHotelId(Date bookingDate, Long hotelId) {
        TypedQuery<HotelBooking> query = em.createNamedQuery(HotelBooking.FIND_BY_BOOKING_DATA_AND_HOTEL_ID, HotelBooking.class)
                .setParameter("booking_date", bookingDate).setParameter("hotel_id",hotelId);
        return query.getSingleResult();
    }


    public List<HotelBooking> findByCustomerEmail(String email) {
        TypedQuery<HotelBooking> query = em.createNamedQuery(HotelBooking.FIND_BY_CUSTOMER_EMAIL, HotelBooking.class).setParameter("email", email);
        return query.getResultList();
    }



    /**
     * <p>Persists the provided HotelBooking object to the application database using the EntityManager.</p>
     *
     * <p>{@link javax.persistence.EntityManager#persist(Object) persist(Object)} takes an entity instance, adds it to the
     * context and makes that instance managed (ie future updates to the entity will be tracked)</p>
     *
     * <p>persist(Object) will set the @GeneratedValue @Id for an object.</p>
     *
     * @param hotelBooking The HotelBooking object to be persisted
     * @return The HotelBooking object that has been persisted
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    public HotelBooking create(HotelBooking hotelBooking) throws Exception {

        log.info("HotelBooking.create() - Creating " + hotelBooking.getHotelId() + " " + hotelBooking.getBookingId()+" " +hotelBooking.getBookingDate());

        // Write the hotelbooking to the database.
        em.persist(hotelBooking);

        return hotelBooking;
    }

    /**
     * <p>Updates an existing HotelBooking object in the application database with the provided HotelBooking object.</p>
     *
     * <p>{@link javax.persistence.EntityManager#merge(Object) merge(Object)} creates a new instance of your entity,
     * copies the state from the supplied entity, and makes the new copy managed. The instance you pass in will not be
     * managed (any changes you make will not be part of the transaction - unless you call merge again).</p>
     *
     * <p>merge(Object) however must have an object with the @Id already generated.</p>
     *
     * @param hotelBooking The HotelBooking object to be merged with an existing HotelBooking
     * @return The HotelBooking that has been merged
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    public HotelBooking update(HotelBooking hotelBooking) throws Exception {
        log.info("HotelRepository.update() - Updating " + hotelBooking.getHotelId() + " " + hotelBooking.getCustomerId()+" " +hotelBooking.getBookingDate());

        // Either update the hotelbooking or add it if it can't be found.
        em.merge(hotelBooking);

        return hotelBooking;
    }

    /**
     * <p>Deletes the provided HotelBooking object from the application database if found there</p>
     *
     * @param hotelBooking The HotelBooking object to be removed from the application database
     * @return The HotelBooking object that has been successfully removed from the application database; or null
     * @throws Exception
     */
    public HotelBooking delete(HotelBooking hotelBooking) throws Exception {
        log.info("HotelRepository.delete() - Deleting " + hotelBooking.getHotelId() + " " + hotelBooking.getCustomerId()+" "  +hotelBooking.getBookingDate());

        if (hotelBooking.getHotelId() != null) {
            /*
             * The Hibernate session (aka EntityManager's persistent context) is closed and invalidated after the commit(),
             * because it is bound to a transaction. The object goes into a detached status. If you open a new persistent
             * context, the object isn't known as in a persistent state in this new context, so you have to merge it.
             *
             * Merge sees that the object has a primary key (id), so it knows it is not new and must hit the database
             * to reattach it.
             *
             * Note, there is NO remove method which would just take a primary key (id) and a entity class as argument.
             * You first need an object in a persistent state to be able to delete it.
             *
             * Therefore we merge first and then we can remove it.
             */
            em.remove(em.merge(hotelBooking));

        } else {
            log.info("HotelRepository.delete() - No ID was found so can't Delete.");
        }

        return hotelBooking;
    }
}
