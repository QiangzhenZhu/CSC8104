package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.repository;

/**
 * @Classname BaseRepository
 * @Description TODO
 * @Date 2022/11/20 15:22
 * @Created by 10835
 */
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@RequestScoped
public class BaseRepository {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;
}
