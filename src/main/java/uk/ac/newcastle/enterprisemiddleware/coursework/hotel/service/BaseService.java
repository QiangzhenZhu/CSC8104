package uk.ac.newcastle.enterprisemiddleware.coursework.hotel.service;

/**
 * @Classname BaseService
 * @Description TODO
 * @Date 2022/11/20 16:29
 * @Created by 10835
 */

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

@Dependent
public class BaseService {
    @Inject
    @Named("logger")
    Logger log;

}
