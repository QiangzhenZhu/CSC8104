package uk.ac.newcastle.enterprisemiddleware.coursework.agent;

import uk.ac.newcastle.enterprisemiddleware.coursework.hotel.repository.BaseRepository;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @Classname TravelAgentBookingRepository
 * @Description TODO
 * @Date 2022/11/21 20:55
 * @Created by 10835
 */
@RequestScoped
public class TravelAgentBookingRepository extends BaseRepository {

    /**
     *
     * @return
     */
    public List<TravelAgentBooking> findAllTravelAgentBook() {
        TypedQuery<TravelAgentBooking> query = em.createNamedQuery(TravelAgentBooking.FIND_TRAVEL_AGENT_BOOKING_ALL, TravelAgentBooking.class);
        return query.getResultList();
    }

    /**
     *
     * @param id
     * @return
     */
    public TravelAgentBooking findTravelAgentBookById(Long id) {
        return em.find(TravelAgentBooking.class, id);
    }

    /**
     *
     * @param customerId
     * @return
     */
    public List<TravelAgentBooking> findAllByCustomer(Long customerId) {
        TypedQuery<TravelAgentBooking> query = em.createNamedQuery(TravelAgentBooking.FIND_TRAVEL_AGENT_BOOKING_BY_CUSTOMER, TravelAgentBooking.class).setParameter("customerId", customerId);
        return query.getResultList();
    }

    /**
     *
     * @param travelAgentBooking
     * @return
     * @throws Exception
     */
    public TravelAgentBooking create(TravelAgentBooking travelAgentBooking) throws Exception {
        log.info("TravelAgentBookingRepository.create() - Updating "+ travelAgentBooking.toString());
        // Write the consumer to the database.
        em.persist(travelAgentBooking);
        return travelAgentBooking;
    }

    /**
     *
     * @param travelAgentBooking
     * @return
     * @throws Exception
     */
    public TravelAgentBooking update(TravelAgentBooking travelAgentBooking) throws Exception {
        log.info("TravelAgentBookingRepository.update() - Updating "+ travelAgentBooking.toString());
        // Either update the contact or add it if it can't be found.
        em.merge(travelAgentBooking);
        return travelAgentBooking;
    }

    /**
     *
     * @param travelAgentBooking
     * @return
     * @throws Exception
     */
    public TravelAgentBooking delete(TravelAgentBooking travelAgentBooking) throws Exception {
        if (travelAgentBooking.getTravelAgentId() != null) {
            em.remove(em.merge(travelAgentBooking));
        } else {
            log.info("TravelAgentBookingRepository.delete() - No ID was found so can't Delete.");
        }
        return travelAgentBooking;
    }
}
