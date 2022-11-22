package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.repository;

import uk.ac.newcastle.enterprisemiddleware.contact.Contact;
import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.entity.Hotel;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @Classname HotelRepository
 * @Description TODO
 * @Date 2022/11/20 15:20
 * @Created by 10835
 */
@RequestScoped
public class HotelRepository extends BaseRepository {
    /**
     * <p>Returns a List of all persisted {@link Contact} objects, sorted alphabetically by last name.</p>
     *
     * @return List of Contact objects
     */
    public List<Hotel> findAllOrderedByName() {
        TypedQuery<Hotel> query = em.createNamedQuery(Hotel.FIND_ALL, Hotel.class);
        return query.getResultList();
    }

    /**
     * <p>Returns a single Contact object, specified by a Long id.<p/>
     *
     * @param id The id field of the Contact to be returned
     * @return The Contact with the specified id
     */
    public Hotel findById(Long id) {
        return em.find(Hotel.class, id);
    }

    /**
     * <p>Returns a single Contact object, specified by a String email.</p>
     *
     * <p>If there is more than one Contact with the specified email, only the first encountered will be returned.<p/>
     *
     * @param phone The email field of the Contact to be returned
     * @return The first Contact with the specified email
     */
    public Hotel findByPhoneNumber(String phone) {
        TypedQuery<Hotel> query = em.createNamedQuery(Hotel.FIND_BY_PHONE_NUMBER, Hotel.class).setParameter("phone_number", phone);
        return query.getSingleResult();
    }

    public List<Hotel>  findAllByName(String name) {
        TypedQuery<Hotel> query = em.createNamedQuery(Hotel.FIND_ALL_BY_NAME, Hotel.class).setParameter("name", name);
        return query.getResultList();
    }

    /**
     * <p>Persists the provided Contact object to the application database using the EntityManager.</p>
     *
     * <p>{@link javax.persistence.EntityManager#persist(Object) persist(Object)} takes an entity instance, adds it to the
     * context and makes that instance managed (ie future updates to the entity will be tracked)</p>
     *
     * <p>persist(Object) will set the @GeneratedValue @Id for an object.</p>
     *
     * @param hotel The Contact object to be persisted
     * @return The Contact object that has been persisted
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    public Hotel create(Hotel hotel) throws Exception {
        log.info("HotelRepository.create() - Creating "+hotel.toString());

        // Write the contact to the database.
        em.persist(hotel);

        return hotel;
    }

    /**
     * <p>Updates an existing Contact object in the application database with the provided Contact object.</p>
     *
     * <p>{@link javax.persistence.EntityManager#merge(Object) merge(Object)} creates a new instance of your entity,
     * copies the state from the supplied entity, and makes the new copy managed. The instance you pass in will not be
     * managed (any changes you make will not be part of the transaction - unless you call merge again).</p>
     *
     * <p>merge(Object) however must have an object with the @Id already generated.</p>
     *
     * @param hotel The Contact object to be merged with an existing Contact
     * @return The Contact that has been merged
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    public Hotel update(Hotel hotel) throws Exception {
        log.info("HotelRepository.update() - Updating " + hotel.getPhoneNumber() + " " + hotel.getPostCode()+" " +hotel.getName());

        // Either update the contact or add it if it can't be found.
        em.merge(hotel);

        return hotel;
    }

    /**
     * <p>Deletes the provided Contact object from the application database if found there</p>
     *
     * @param hotel The Contact object to be removed from the application database
     * @return The Contact object that has been successfully removed from the application database; or null
     * @throws Exception
     */
    public Hotel delete(Hotel hotel) throws Exception {
        log.info("HotelRepository.delete() - Deleting " + hotel.getName() + " " + hotel.getPhoneNumber()+" " +hotel.getName());

        if (hotel.getHotelId() != null) {
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
            em.remove(em.merge(hotel));

        } else {
            log.info("HotelRepository.delete() - No ID was found so can't Delete.");
        }

        return hotel;
    }
}
